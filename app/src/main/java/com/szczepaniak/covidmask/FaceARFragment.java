package com.szczepaniak.covidmask;

import com.google.ar.core.Frame;
import com.google.ar.core.LightEstimate;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.ux.ArFragment;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.google.ar.core.Config;
import com.google.ar.core.Config.AugmentedFaceMode;
import com.google.ar.core.Session;
import java.util.EnumSet;
import java.util.Set;
public class FaceARFragment extends ArFragment {

    private Session session;
    private Config config;

    @Override
    protected Set<Session.Feature> getSessionFeatures() {
        return EnumSet.of(Session.Feature.FRONT_CAMERA);
    }

    @Override
    protected Config getSessionConfiguration(Session session) {
        this.session = session;
        this.config =  new Config(session);
        config.setAugmentedFaceMode(AugmentedFaceMode.MESH3D);
        config.setLightEstimationMode(Config.LightEstimationMode.AMBIENT_INTENSITY);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        return config;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout =
                (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);
        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);

        return frameLayout;
    }

//    @Override
//    public void onUpdate(FrameTime frameTime) {
//        super.onUpdate(frameTime);
//        Frame frame = null;
//        try {
//            frame = session.update();
//            // Get the light estimate for the current frame.
//            LightEstimate lightEstimate = frame.getLightEstimate();
//
//            // Get intensity and direction of the main directional light from the current light estimate.
//            float[] intensity = lightEstimate.getEnvironmentalHdrMainLightIntensity(); // note - currently only out param.
//            float[] direction = lightEstimate.getEnvironmentalHdrMainLightDirection();
//            app.setDirectionalLightValues(intensity, direction); // app-specific code.
//
//            // Get ambient lighting as spherical harmonics coefficients.
//            float[] harmonics = lightEstimate.getEnvironmentalHdrAmbientSphericalHarmonics();
//            app.setAmbientSphericalHarmonicsLightValues(harmonics); // app-specific code.
//
//            // Get HDR environmental lighting as a cubemap in linear color space.
//            Image[] lightmaps = lightEstimate.acquireEnvironmentalHdrCubeMap();
//            for (int i = 0; i < lightmaps.length /*should be 6*/; ++i) {
//                app.UploadToTexture(i, lightmaps[i]);  // app-specific code.
//            }
//        } catch (CameraNotAvailableException e) {
//            e.printStackTrace();
//        }
//
//
//    }
}
