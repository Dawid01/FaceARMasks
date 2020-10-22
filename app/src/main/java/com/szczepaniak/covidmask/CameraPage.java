package com.szczepaniak.covidmask;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Frame;
import com.google.ar.core.LightEstimate;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class CameraPage extends Fragment {

    private FaceARFragment arFragment;
    private ModelRenderable modelRenderable;
    private Texture texture;
    private  boolean isAdded = false;
    float[] colorCorrection = new float[4];
    private Vibrator vibrator;
    private VideoButton videoButton;
    private ImageView galleryButton;
    private MainActivity m;


    public CameraPage(View pageView, MainActivity mainActivity) {

        this.m = mainActivity;
        vibrator = (Vibrator) m.getSystemService(Context.VIBRATOR_SERVICE);

        arFragment = (FaceARFragment) m.getSupportFragmentManager().findFragmentById(R.id.ar_fragment);
        MaskModel covidMask = new MaskModel();
        covidMask.setModelResource(R.raw.maskcovid);
        covidMask.setTextureResource(R.drawable.transparent);
        covidMask.setLocalPosition(new Vector3(0f, -0.105f, 0.004f));
        covidMask.setLocalScale(new Vector3(1f, 1f, 1f));
        loadMaskModel(covidMask);

        videoButton = pageView.findViewById(R.id.video_button);

        videoButton.setVideButtonListner(new VideButtonListner() {
            @Override
            void takePhoto() {
                takePhotoAR();
                vibrator.vibrate(25);
            }

            @Override
            void startRecording() {
                vibrator.vibrate(25);

            }

            @Override
            void stopRecording() {
                vibrator.vibrate(25);
            }
        });

        galleryButton = pageView.findViewById(R.id.gallery_button);
        updateGalleryBtm(null);

    }




    private void takePhotoAR() {

        final Bitmap bitmap = Bitmap.createBitmap(arFragment.getView().getWidth(), arFragment.getView().getHeight(),
                Bitmap.Config.ARGB_8888);

        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        PixelCopy.request((SurfaceView) arFragment.getArSceneView(), bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                Log.e("Photo AR",bitmap.toString());
                saveImage(bitmap);
            } else {
                Toast toast = Toast.makeText(m,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }

    private void loadMaskModel(MaskModel maskModel){

        ModelRenderable.builder()
                .setSource(m, maskModel.getModelResource())
                .build()
                .thenAccept(renderable -> {
                    modelRenderable = renderable;
                    modelRenderable.setShadowCaster(false);
                    modelRenderable.setShadowReceiver(true);
                });

        Texture.builder()
                .setSource(m, maskModel.getTextureResource())
                .build()
                .thenAccept(texture -> this.texture = texture);
        arFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);
        arFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {


            if(modelRenderable == null || texture == null){
                return;
            }

            Frame frame = arFragment.getArSceneView().getArFrame();
            assert frame != null;
            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

            for (AugmentedFace augmentedFace : augmentedFaces){
                if(isAdded)
                    return;

                AugmentedFaceNode arugmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                arugmentedFaceNode.setParent(arFragment.getArSceneView().getScene());
                Node node = new Node();
                //modelRenderable.setRenderPriority(Renderable.RENDER_PRIORITY_FIRST);
                node.setRenderable(modelRenderable);
                node.setParent(arugmentedFaceNode);
                node.setLocalScale(maskModel.getLocalScale());
                node.setLocalPosition(maskModel.getLocalPosition());

                arugmentedFaceNode.setFaceMeshTexture(texture);
                isAdded = true;
            }

            LightEstimate lightEstimate = frame.getLightEstimate();
            float pixelIntensity = lightEstimate.getPixelIntensity();
            lightEstimate.getColorCorrection(colorCorrection, 0);
        });
    }

    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM ).toString();
        File myDir = new File(root + "/CovidMask");

        try {
            myDir.mkdirs();
            String name = String.valueOf("CovidMask-" + System.currentTimeMillis() + ".png");
            File file = new File(myDir, name);
            if(!file.exists()) {
                file.createNewFile();
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos);
            //finalBitmap.compress(Bitmap.CompressFormat.JPEG, 1 , bos);

            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            updateGalleryBtm(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateGalleryBtm(File photo){

        clearGlideDiskCache();

        File[] files = getGalleryFiles();
        if(files != null) {
            if(files.length !=0) {
                if(photo == null) {
                    Glide.with(m).load(files[0]).centerCrop().circleCrop().apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(galleryButton);
                }else{
                    Glide.with(m).load(photo).skipMemoryCache(true).centerCrop().circleCrop().apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(galleryButton);
                    //  Picasso.get().load(photo).memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop().transform(new CircleTransform()).into(galleryButton);

                }

            }else {
                Glide.with(m).load(R.drawable.gallery_bg).skipMemoryCache(true).centerCrop().circleCrop().apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(galleryButton);
            }

        }else {
            Glide.with(m).load(R.drawable.gallery_bg).skipMemoryCache(true).centerCrop().circleCrop().apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(galleryButton);

        }
    }

    void clearGlideDiskCache()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Glide.get(m).clearDiskCache();
            }
        }).start();
    }

    private File[] getGalleryFiles(){

        String path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM ).toString() +"/CovidMask";
        File directory = new File(path);
        File[] files = directory.listFiles();

        Arrays.sort(files, new Comparator() {
            public int compare(Object o1, Object o2) {

                if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                    return -1;
                } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }

        });

        return  files;
    }
}
