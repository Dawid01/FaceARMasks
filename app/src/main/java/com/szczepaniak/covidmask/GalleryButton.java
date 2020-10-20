package com.szczepaniak.covidmask;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GalleryButton  extends FrameLayout {


    public GalleryButton(@NonNull Context context) {
        super(context);
        initView();
    }

    public GalleryButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public GalleryButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GalleryButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView() {
        inflate(getContext(), R.layout.video_button, this);
    }
}
