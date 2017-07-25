package com.fox.pinrenpin.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fox.pinrenpin.MyApplication;
import com.fox.pinrenpin.R;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MagicFox on 2016/8/18.
 */
public class PlaneView  extends RelativeLayout {


    public PlaneView(Context context) {
        super(context);
        init(context);
    }

    public PlaneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    TextView plane;

    private static int[] textOffsetY = new int[] { 0, 0, -1, -2, -2, -1 };
    private Handler handler;
    private Animation animIn;
    private Animation animOut;
    private boolean goToLink = false;
    /** 飞机间隔时间：3分钟 */
    private static final int INTERVAL = 1 * 60 * 1000;

    /** 飞机停留时间：3秒 */
    private static final int STOP_TIME = 10 * 1000;
    private void init(Context context) {
        plane = new TextView(context);
        plane.setGravity(Gravity.CENTER_VERTICAL);
        plane.setTextColor(0xFFFFFFFF);
        plane.setTextSize(14);
        addView(plane);
        plane.setBackgroundResource(R.drawable.laba_plane);


        final int paddingLeft = plane.getPaddingLeft();
        final int paddingRight = plane.getPaddingRight();
        final int paddingTop = plane.getPaddingTop();
        final int paddingBottom = plane.getPaddingBottom();

        final AnimationDrawable drawable = (AnimationDrawable) plane.getBackground();
        drawable.start();
        final int duration = drawable.getDuration(0);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i = 0;i < drawable.getNumberOfFrames();i++){
                    if(drawable.getCurrent().equals(drawable.getFrame(i))){
                        plane.setPadding(paddingLeft,paddingTop+dipToPixel(textOffsetY[i]),paddingRight,paddingBottom-dipToPixel(textOffsetY[i]));
                        plane.invalidate();
                    }
                }
                handler.postDelayed(this,duration);
            }
        },duration);

        animIn = AnimationUtils.loadAnimation(context, R.anim.laba_plane_in);
        animIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                goToLink = true;
                plane.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                plane.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        plane.startAnimation(animOut);
                    }
                }, STOP_TIME);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animOut = AnimationUtils.loadAnimation(context,R.anim.laba_plane_out);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goToLink = false;
                plane.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        plane.setText("很长很长的广告");
        plane.setVisibility(View.INVISIBLE);
    }


    public static int dipToPixel(final float dip) {
        DisplayMetrics metrics;
        metrics = MyApplication.getInstance().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
    }

    private Timer timer;
    private List<String> adsList;
    private int currentAds = 0;
    public void setAdsList(List<String> adsList) {
        this.adsList = adsList;
    }

    /**
     * 开始飞机动画
     */
    public void start() {
        if (timer == null) {
            timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    PlaneView.this.post(new Runnable() {

                        @Override
                        public void run() {
                            if (adsList != null && adsList.size() > 0) {
                                currentAds = new Random().nextInt(adsList.size());
                                plane.setText(adsList.get(currentAds));
                            }
                            plane.startAnimation(animIn);
                        }
                    });
                }
            };
            timer.schedule(task, INTERVAL, INTERVAL);
        }
    }

    /**
     * 取消飞机动画
     */
    public void cancel() {
        if (timer != null) {
            try {
                timer.cancel();
            } catch (Exception e) {

            } finally {
                timer = null;
            }
        }
    }
}
