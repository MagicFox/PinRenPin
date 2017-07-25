package com.fox.pinrenpin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ImageButton;

import com.fox.pinrenpin.R;

/**
 * Created by MagicFox on 2016/8/18.
 */
public class GoButton  extends ImageButton {

    public static interface OnGoClickListener {
        void onClick();
    }

    private OnGoClickListener onGoClickListener;

    public void setOnGoClickListener(OnGoClickListener onGoClickListener) {
        this.onGoClickListener = onGoClickListener;
    }

    public GoButton(Context context) {
        super(context);
    }

    public GoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.drawable.laba_go2);
                requestParentDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                super.setEnabled(false);
                postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        setBackgroundResource(R.drawable.laba_go3);
                    }
                }, 100);
                if (onGoClickListener != null) {
                    onGoClickListener.onClick();
                }
                requestParentDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                requestParentDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        setBackgroundResource(enabled ? R.drawable.laba_go1 : R.drawable.laba_go3);
        super.setEnabled(enabled);
    }

    private void requestParentDisallowInterceptTouchEvent(boolean isAllow) {
        ViewParent p = getParent();
        if (p != null) {
            p.requestDisallowInterceptTouchEvent(isAllow);
        }
    }

}
