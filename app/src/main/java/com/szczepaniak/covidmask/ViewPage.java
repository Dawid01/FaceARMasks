package com.szczepaniak.covidmask;

public enum ViewPage {


    GALLERY(R.string.app_name, R.layout.gallery_layout),
    CAMERA(R.string.app_name, R.layout.camera_layout),
    FILTERS(R.string.app_name, R.layout.filters_layout);


    private int mTitleResId;
    private int mLayoutResId;

    ViewPage(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
