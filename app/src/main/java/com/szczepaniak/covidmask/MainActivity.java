package com.szczepaniak.covidmask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import com.szczepaniak.covidmask.helpers.PermissionHelper;
import com.szczepaniak.covidmask.helpers.FullScreenHelper;

public class MainActivity extends AppCompatActivity{


    private ViewPager viewPager;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setSaveEnabled(true);
        viewPager.setCurrentItem(1);
        viewPager.setAdapter(new PageAdapter(this));

        viewPager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });

        //viewPager.setCurrentItem(1);

    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionHelper.hasCameraPermission(this)) {
            PermissionHelper.requestCameraPermission(this);
            return;
        }

        if (!PermissionHelper.hasWritePermission(this)) {
            PermissionHelper.requestWritePermission(this);
            return;
        }

        if (!PermissionHelper.hasReadPermission(this)) {
            PermissionHelper.requestWritePermission(this);
            return;
        }

    }

    @Override
    public void onPause() {
        super.onPause(); 
    }


}
