package com.szczepaniak.hairapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.LightEstimate;
import com.google.ar.core.Plane;
import com.google.ar.core.PointCloud;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.szczepaniak.hairapp.helpers.CameraPermissionHelper;
import com.szczepaniak.hairapp.helpers.FullScreenHelper;
import com.szczepaniak.hairapp.helpers.SnackbarHelper;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.AugmentedFace;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity{

    private FaceARFragment arFragment;
    private ModelRenderable modelRenderable;
    private Texture texture;
    private Texture hairTexture;
    private  boolean isAdded = false;
    float[] colorCorrection = new float[4];


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        arFragment = (FaceARFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);
        ModelRenderable.builder()
                .setSource(this, R.raw.mask)
                .build()
                .thenAccept(renderable -> {
                    modelRenderable = renderable;
                    modelRenderable.setShadowCaster(false);
                    modelRenderable.setShadowReceiver(true);
                });

        Texture.builder()
                .setSource(this, R.drawable.transparent)
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
               // arugmentedFaceNode.setFaceRegionsRenderable(null);
                Node node = new Node();
                modelRenderable.setRenderPriority(Renderable.RENDER_PRIORITY_FIRST);
                node.setRenderable(modelRenderable);
                node.setParent(arugmentedFaceNode);
                //node.setLocalPosition(new Vector3(0f, -0.06f, -0.07f));
                node.setLocalScale(new Vector3(0.9f, 0.9f, 0.9f));
                node.setLocalPosition(new Vector3(0f, -0.105f, 0.02f));
                arugmentedFaceNode.setFaceMeshTexture(texture);
                isAdded = true;
            }

            LightEstimate lightEstimate = frame.getLightEstimate();

            // Get the pixel intensity of AMBIENT_INTENSITY mode.
            float pixelIntensity = lightEstimate.getPixelIntensity();

            // Get the pixel color correction of AMBIENT_INTENSITY mode.
            lightEstimate.getColorCorrection(colorCorrection, 0);
        });



    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this);
            return;
        }

    }

    @Override
    public void onPause() {
        super.onPause(); 
    }




}
