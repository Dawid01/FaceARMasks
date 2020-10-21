package com.szczepaniak.covidmask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.ar.core.exceptions.NotYetAvailableException;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.logging.Handler;

public class VideoButton extends FrameLayout {

    private ImageView button;
    private CircularProgressBar progressBar;
    private boolean isRecording = false;

    public VideButtonListner getVideButtonListner() {
        return videButtonListner;
    }

    public void setVideButtonListner(VideButtonListner videButtonListner) {
        this.videButtonListner = videButtonListner;
    }

    private VideButtonListner videButtonListner;

    public VideoButton(Context context) {
        super(context);
        initView();
    }

    public VideoButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VideoButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public VideoButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.video_button, this);
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progress);
        ConstraintLayout.LayoutParams param = (ConstraintLayout.LayoutParams) progressBar.getLayoutParams();
        int w = param.width;
        int h = param.height;
        float wp = progressBar.getProgressBarWidth();
        float wb = progressBar.getBackgroundProgressBarWidth();


        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    videButtonListner.takePhoto();
                } catch (NotYetAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

        button.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                videButtonListner.startRecording();
                CountDownTimer countDownTimer;
                param.width = (int)(w * 1.5f);
                param.height = (int)(h * 1.5f);
                progressBar.setLayoutParams(param);
                button.setScaleX(0.75f);
                button.setScaleY(0.75f);
                progressBar.setProgressBarWidth(wp * 1.2f);
                progressBar.setBackgroundProgressBarWidth(wb * 1.2f);
                isRecording = true;
                countDownTimer= new CountDownTimer(10000, 1) {

                    public void onTick(long millisUntilFinished) {
                        int progress = (int)((10000-millisUntilFinished)/100);
                        progressBar.setProgress(progress);
                    }

                    public void onFinish() {
                      progressBar.clearAnimation();
                      progressBar.setProgress(0);
                      param.width = w;
                      param.height = h;
                      progressBar.setLayoutParams(param);
                      progressBar.setProgressBarWidth(wp);
                      progressBar.setBackgroundProgressBarWidth(wb);
                      button.setScaleX(1f);
                      button.setScaleY(1f);
                      if(isRecording) {
                          videButtonListner.stopRecording();
                      }
                      isRecording = false;
                    }
                }.start();

                button.setOnTouchListener(new OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP){
                            if(isRecording){
                                countDownTimer.onFinish();
                                countDownTimer.cancel();
                            }

                        }
                        return false;
                    }
                });
                return false;
            }
        });

    }

}
