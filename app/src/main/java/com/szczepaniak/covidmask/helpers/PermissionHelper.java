package com.szczepaniak.covidmask.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHelper {
    private static final int CAMERA_PERMISSION_CODE = 0;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final int WRITE_STORAGE_PERMISSION_CODE = 0;
    private static final String WRITE_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int READ_STORAGE_PERMISSION_CODE = 0;
    private static final String READ_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

    public static boolean hasCameraPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, CAMERA_PERMISSION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasWritePermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, WRITE_STORAGE_PERMISSION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasReadPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, READ_STORAGE_PERMISSION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity, new String[] {CAMERA_PERMISSION}, CAMERA_PERMISSION_CODE);
    }

    public static void requestWritePermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity, new String[] {WRITE_STORAGE_PERMISSION}, WRITE_STORAGE_PERMISSION_CODE);
    }

    public static void requestReadPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity, new String[] {READ_STORAGE_PERMISSION}, READ_STORAGE_PERMISSION_CODE);
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, CAMERA_PERMISSION);
    }


    public static void launchPermissionSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent);
    }


}
