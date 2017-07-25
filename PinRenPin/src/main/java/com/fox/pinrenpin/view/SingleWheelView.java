package com.fox.pinrenpin.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by MagicFox on 2016/8/18.
 */
public class SingleWheelView extends GameEngine {

    private int viewWidth;
    private int viewHeight;

    private List<Bitmap> bmpList;

    /* 摄像机位置 */
    private int cameraY = 0;

    /* 起始位置 */
    private final int INIT_POSITON = WheelView.INDEX_LEMON;

    public SingleWheelView(Context context) {
        super(context);
        init();
    }

    public SingleWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    public void setBmpList(List<Bitmap> bmpList) {
        this.bmpList = bmpList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int relativeCameraOffY = cameraY % slotTotalHeight;
        if (relativeCameraOffY < 0) {
            relativeCameraOffY += slotTotalHeight;
        }
        int start = relativeCameraOffY / slotHeight;
        int end = (relativeCameraOffY + viewHeight) / slotHeight;
        if (end < bmpList.size()) {
            for (int i = start; i <= end; i++) {
                int offset = slotHeight * i - relativeCameraOffY;
                canvas.drawBitmap(bmpList.get(i), (viewWidth - slotWidth) / 2, offset, null);
            }
        } else {
            for (int i = start; i < bmpList.size(); i++) {
                int offset = slotHeight * i - relativeCameraOffY;
                canvas.drawBitmap(bmpList.get(i), (viewWidth - slotWidth) / 2, offset, null);
            }

            int extend = end - bmpList.size();
            for (int i = 0; i <= extend; i++) {
                int offset = slotHeight * i + slotTotalHeight - relativeCameraOffY;
                canvas.drawBitmap(bmpList.get(i), (viewWidth - slotWidth) / 2, offset, null);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getWidth();
        viewHeight = getHeight();

        cameraY = slotHeight * INIT_POSITON + (slotHeight / 2 - viewHeight / 2);
        onCameraYChanged(cameraY);
    }

    @Override
    protected void onCameraYChanged(int cameraY) {
        super.onCameraYChanged(cameraY);
        this.cameraY = cameraY;
        postInvalidate();
    }

}
