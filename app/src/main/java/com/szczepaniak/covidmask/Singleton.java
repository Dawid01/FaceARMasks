package com.szczepaniak.covidmask;

import android.os.Environment;

import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class Singleton {

    private static Singleton singleton;

    private GalleryAdapter galleryAdapter;
    private ViewPager viewPager;

    private Singleton(){}

    public static Singleton getInstance(){
        if (singleton == null){
            singleton = new Singleton();
        }

        return singleton;
    }

    public File[] getGalleryFiles(){

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

    public GalleryAdapter getGalleryAdapter() {
        return galleryAdapter;
    }

    public void setGalleryAdapter(GalleryAdapter galleryAdapter) {
        this.galleryAdapter = galleryAdapter;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
}

