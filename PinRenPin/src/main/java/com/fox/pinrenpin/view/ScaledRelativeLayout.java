package com.fox.pinrenpin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.fox.pinrenpin.R;

/**
 * Created by MagicFox on 2016/8/18.
 */
public class ScaledRelativeLayout extends RelativeLayout {
    private final int widthScale;
    private final int heightScale;

    public ScaledRelativeLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.scaledLayout);
        widthScale = a.getInt(R.styleable.scaledLayout_widthScale, 8);
        heightScale = a.getInt(R.styleable.scaledLayout_heightScale, 3);
        a.recycle();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width * heightScale / widthScale;
        if (height == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }
    }
}
