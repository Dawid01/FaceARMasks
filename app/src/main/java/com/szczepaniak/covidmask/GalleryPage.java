package com.szczepaniak.covidmask;

import android.os.Environment;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class GalleryPage extends Fragment {

    private MainActivity m;
    private RecyclerView galleryView;

    public GalleryPage(View pageView, MainActivity m) {
        this.m = m;

        galleryView = pageView.findViewById(R.id.gallery);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(m, 3);
        galleryView.setLayoutManager(gridLayoutManager);
        galleryView.setHasFixedSize(true);
        galleryView.setItemViewCacheSize(255);
        File[] files = getGalleryFiles();
        GalleryAdapter galleryAdapter = new GalleryAdapter(files, m, galleryView);
        galleryView.setAdapter(galleryAdapter);

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
