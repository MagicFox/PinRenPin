package com.fox.pinrenpin.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fox.pinrenpin.R;

/**
 * Created by MagicFox on 2016/8/18.
 */
public class LabaProgressBarView extends LinearLayout {
    private Context context;

    public LabaProgressBarView(Context context) {
        super(context);
        init();
        this.context = context;
    }

    public LabaProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        this.context = context;
    }

    private void init() {
        setBackgroundResource(R.drawable.laba_progress_bg);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setProgress(final double progressScale) {
        post(new Runnable() {

            @Override
            public void run() {
                removeAllViews();
                ImageView progressBar = new ImageView(context);
                progressBar.setBackgroundResource(R.drawable.laba_progress_full_bg);
                int total = getWidth();
                int progressWidth = (int) (total * progressScale);
                addView(progressBar, new LinearLayout.LayoutParams(progressWidth,
                        LinearLayout.LayoutParams.MATCH_PARENT));
            }
        });
    }
}