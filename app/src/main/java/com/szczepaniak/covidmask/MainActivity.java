package com.szczepaniak.covidmask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.szczepaniak.covidmask.helpers.PermissionHelper;
import com.szczepaniak.covidmask.helpers.FullScreenHelper;

import java.io.File;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity{


    private ViewPager viewPager;
    private Singleton singleton;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);
       // viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(25);
//        viewPager.setPageTransformer(true, new DepthPageTransformer(), 2);
        PageAdapter pagerAdapter = new PageAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
        singleton = Singleton.getInstance();
        singleton.setViewPager(viewPager);



        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {

                    File[] files = singleton.getGalleryFiles();
                    GalleryAdapter galleryAdapter = singleton.getGalleryAdapter();
                    if(!Arrays.equals(files, galleryAdapter.getFiles())) {
                        galleryAdapter.setFiles(files);
                        galleryAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
       // pagerAdapter.notifyDataSetChanged();



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
