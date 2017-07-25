package com.fox.pinrenpin.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fox.pinrenpin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 赢得积分控件
 * Created by MagicFox on 2016/8/18.
 */
public class WinScoreView extends ImageView{

    public WinScoreView(Context context) {
        super(context);
        init(context);
    }

    public WinScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private List<Bitmap> bmpList;
    private int itemWidth,itemHeight;
    private Paint paint;

    /** 动画时间 ms */
    private static final int ANIM_TIME = 350;

    /** 最少显示4位 */
    private static final int MIN_SIZE = 4;
    int currentSocre = 0,targetScore = 0;
    private long animStartTime = -1L;

    private void init(Context context) {
        bmpList = new ArrayList<>(10);
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_slot_0));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_slot_1));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_slot_2));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_slot_3));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_slot_4));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_slot_5));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_slot_6));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_slot_7));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_slot_8));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_slot_9));

        itemWidth = bmpList.get(0).getWidth();
        itemHeight = bmpList.get(0).getHeight();

        paint = new Paint();

    }

    List<Integer> changeList;
    @Override
    protected void onDraw(Canvas canvas) {
        if(animStartTime > 0 && targetScore != currentSocre){
            int ainmCount = changeList.size();
            long duration = System.currentTimeMillis() - animStartTime;
            int currentAnimIndex = (int)(duration/ANIM_TIME);
            if(currentAnimIndex < ainmCount){
                drawScoreLeft(canvas,currentSocre,changeList.get(currentAnimIndex));
                drawScoreRight(canvas,currentSocre,targetScore,changeList.get(currentAnimIndex));
                float percent = ((float) ((duration % ANIM_TIME))) / ANIM_TIME;
                drawScoreAnim(canvas,currentSocre,targetScore,changeList.get(currentAnimIndex),percent);
                invalidate();
            } else {
                animStartTime = -1;
                currentSocre =targetScore;
                drawScoreLeft(canvas,currentSocre,-1);
            }
        } else {
            drawScoreLeft(canvas,currentSocre,-1);
        }
        super.onDraw(canvas);

    }

    /**
     * 单个数字动画，以currentScore右边为基点
     *
     * @param canvas
     *            画布
     * @param currentScore
     *            当前积分
     * @param targetScore
     *            目标积分
     * @param index
     *            索引
     * @param percent
     *            动画百分比
     */
    private void drawScoreAnim(Canvas canvas, int currentScore, int targetScore, int index, float percent) {
        int size = getSize(currentScore);
        int rightEdge = (getWidth() + itemWidth * size) / 2;

        int digitalOut = getDigitalByIndex(currentScore, index);
        canvas.drawBitmap(bmpList.get(digitalOut), rightEdge - itemWidth * (index + 1), (getHeight() - itemHeight) / 2
                - getHeight() * percent, paint);

        int digitalIn = getDigitalByIndex(targetScore, index);
        canvas.drawBitmap(bmpList.get(digitalIn), rightEdge - itemWidth * (index + 1), (getHeight() - itemHeight) / 2
                + getHeight() * (1 - percent), paint);
    }

    /**
     * 画分数的右半部分，以currentScore右边为基点，实际画targetScore
     *
     * @param canvas
     * @param currentScore
     * @param targetScore
     * @param index
     */
    private void drawScoreRight(Canvas canvas, int currentScore, int targetScore, int index) {
        int currentSize = getSize(currentScore);
        int targetSize = getSize(currentScore);
        int rightEdge = (getWidth() + itemWidth * currentSize) / 2;
        for (int i = 0; i < targetSize; i++) {
            if (i < index) {
                int digital = getDigitalByIndex(targetScore, i);
                canvas.drawBitmap(bmpList.get(digital), rightEdge - itemWidth, (getHeight() - itemHeight) / 2, paint);
            }
            rightEdge -= itemWidth;
        }
    }

    /**
     * 画分数的左边部分
     *
     * @param canvas
     * @param score
     * @param index
     *            动画位置
     */
    private void drawScoreLeft(Canvas canvas, int score, int index) {
        int size = getSize(score);
        int rightEdge = (getWidth() + itemWidth * size) / 2;
        for (int i = 0; i < size; i++) {
            if (i > index) {
                int digital = getDigitalByIndex(score, i);
                canvas.drawBitmap(bmpList.get(digital), rightEdge - itemWidth, (getHeight() - itemHeight) / 2, paint);
            }
            rightEdge -= itemWidth;
        }
    }

    /**
     * 获取倒数第index位置的数字
     *
     * @param score
     *            分数
     * @param index
     *            从末尾倒数位置
     */
    private int getDigitalByIndex(int score, int index) {
        for (int i = 0; i < index; i++) {
            score /= 10;
        }
        return score % 10;
    }


    private void startAnim() {
        animStartTime = System.currentTimeMillis();
        invalidate();
    }
    /**
     * 设置分数并开始动画
     *
     * @param score
     */
    public void setScore(int score) {
        this.targetScore = score;
        if (targetScore != currentSocre) {
            startAnim();
            changeList = getChangeList(currentSocre, targetScore);
        }
        invalidate();
    }
    private List<Integer> getChangeList(int score, int targetScore) {
        List<Integer> changeList = new ArrayList<Integer>();

        int size = Math.max(getSize(score), getSize(targetScore));

        for (int i = 0; i < size; i++) {
            if (score % 10 != targetScore % 10) {
                changeList.add(i);
            }
            score /= 10;
            targetScore /= 10;
        }
        return changeList;
    }

    /**
     * 分数位数，最小4位
     *
     * @param score
     * @return
     */
    private int getSize(int score) {
        int size = 0;
        while (score > 0) {
            score /= 10;
            size++;
        }
        if (size < MIN_SIZE) {
            size = MIN_SIZE;
        }
        return size;
    }
}
