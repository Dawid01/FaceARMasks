package com.szczepaniak.covidmask;

import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

public class DepthPageTransformer implements ViewPager.PageTransformer {

    public void transformPage(View view, float position) {
        int pageIndex = (int) view.getTag();
        if (pageIndex == 1) {
            float currentTranslation = -position * view.getWidth();
            ViewCompat.setTranslationX(view, currentTranslation);
            ViewCompat.setTranslationZ(view, -1);
            return;
        }
    }
}
