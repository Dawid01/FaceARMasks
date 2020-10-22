package com.szczepaniak.covidmask;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;


public class PageAdapter extends PagerAdapter {

    private MainActivity m;

    public PageAdapter(MainActivity mainActivity) {
        this.m = mainActivity;
    }

    @NotNull
    @SuppressLint("NonConstantResourceId")
    @Override
    public Object instantiateItem(@NotNull ViewGroup collection, int position) {
        ViewPage page = ViewPage.values()[position];
        LayoutInflater inflater = LayoutInflater.from(m);
        ViewGroup layout = (ViewGroup) inflater.inflate(page.getLayoutResId(), collection, false);
        collection.addView(layout);

        switch (page.getLayoutResId()){

            case R.layout.gallery_layout:
                new GalleryPage(layout, m);
                break;
            case R.layout.camera_layout:
                new CameraPage(layout, m);
                break;
            case R.layout.filters_layout:
                new FiltersPage(layout, m);
                break;

        }

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ViewPage customPagerEnum = ViewPage.values()[position];
        return m.getString(customPagerEnum.getTitleResId());
    }

}