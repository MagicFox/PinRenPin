package com.fox.pinrenpin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ImageView;

import com.fox.pinrenpin.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MagicFox on 2016/8/18.
 */
public class ReduceScoreButton extends ImageView {

    private boolean isPressed = false;

    public static interface OnReduceTriggerListener {
        void onReduce();
    }

    private OnReduceTriggerListener onReduceTriggerListener;

    public void setOnReduceTriggerListener(OnReduceTriggerListener onReduceTriggerListener) {
        this.onReduceTriggerListener = onReduceTriggerListener;
    }

    private static class MyTimer extends Timer {
        private boolean isCanceled = false;

        @Override
        public void cancel() {
            isCanceled = true;
            super.cancel();
        }

        public boolean isCanceled() {
            return isCanceled;
        }
    }

    private MyTimer timer;

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            post(new Runnable() {

                @Override
                public void run() {
                    if (onReduceTriggerListener != null) {
                        onReduceTriggerListener.onReduce();
                    }
                }
            });
        }
    }

    public ReduceScoreButton(Context context) {
        super(context);
        init();
    }

    public ReduceScoreButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReduceScoreButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            isPressed = false;
            return false;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                setImageResource(R.drawable.laba_reduce_selected);
                startTimer();
                requestParentDisallowInterceptTouchEvent(true);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                isPressed = false;
                setImageResource(R.drawable.laba_reduce_normal);
                cancelTimer();
                requestParentDisallowInterceptTouchEvent(false);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void requestParentDisallowInterceptTouchEvent(boolean isAllow) {
        ViewParent p = getParent();
        if (p != null) {
            p.requestDisallowInterceptTouchEvent(isAllow);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            setImageResource(isPressed ? R.drawable.laba_reduce_selected : R.drawable.laba_reduce_normal);
        } else {
            isPressed = false;
            cancelTimer();
            setImageResource(R.drawable.laba_reduce_disable);
        }
        super.setEnabled(enabled);
    }

    private void startTimer() {
        try {
            if (timer == null || timer.isCanceled()) {
                timer = new MyTimer();
                timer.schedule(new MyTimerTask(), 0, 500);
            }
        } catch (Exception e) {
        }
    }

    private void cancelTimer() {
        try {
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {
        }
    }
}