package com.fox.pinrenpin.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.fox.pinrenpin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MagicFox on 2016/8/18.
 */
public class WheelView extends RelativeLayout {

    public static final int INDEX_LEMON = 0;
    public static final int INDEX_MANGO = 1;
    public static final int INDEX_STRAWBERRY = 2;
    public static final int INDEX_GOLD = 3;
    public static final int INDEX_WATERMELON = 4;
    public static final int INDEX_UNKNOWN = 5;

    public static final String INDEX_LEMON_UPLOAD = "4";
    public static final String INDEX_MANGO_UPLOAD = "6";
    public static final String INDEX_STRAWBERRY_UPLOAD = "3";
    public static final String INDEX_GOLD_UPLOAD = "2";
    public static final String INDEX_WATERMELON_UPLOAD = "5";
    public static final String INDEX_UNKNOWN_UPLOAD = "1";

    public static HashMap<Integer, String> map;

    private SingleWheelView wheel1;
    private SingleWheelView wheel2;
    private SingleWheelView wheel3;

    public static interface OnStateChangedListener {
        void onStop();

        void onTriggerStopSound();
    }

    private OnStateChangedListener listener;
    private OnStateChangedListener listener1;
    private OnStateChangedListener listener2;

    public void setOnStateChangedListener(OnStateChangedListener listener) {
        this.listener = listener;
    }

    public void setOnStateChangedListener1(OnStateChangedListener listener1) {
        this.listener1 = listener1;
    }

    public void setOnStateChangedListener2(OnStateChangedListener listener2) {
        this.listener2 = listener2;
    }

    static {
        map = new HashMap<Integer, String>();
        map.put(INDEX_LEMON, INDEX_LEMON_UPLOAD);
        map.put(INDEX_MANGO, INDEX_MANGO_UPLOAD);
        map.put(INDEX_STRAWBERRY, INDEX_STRAWBERRY_UPLOAD);
        map.put(INDEX_GOLD, INDEX_GOLD_UPLOAD);
        map.put(INDEX_WATERMELON, INDEX_WATERMELON_UPLOAD);
        map.put(INDEX_UNKNOWN, INDEX_UNKNOWN_UPLOAD);
    }

    public WheelView(Context context) {
        super(context);
        init();
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        List<Bitmap> bmpList = new ArrayList<Bitmap>();
        Bitmap lemon = BitmapFactory.decodeResource(getResources(), R.drawable.laba_lemon);
        bmpList.add(lemon);
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_mango));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_strawberry));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_gold));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_watermelon));
        bmpList.add(BitmapFactory.decodeResource(getResources(), R.drawable.laba_unknown));

        int slotWidth = lemon.getWidth();
        int slotHeight = lemon.getHeight();

        LayoutInflater.from(getContext()).inflate(R.layout.layout_laba_wheel_view, this);
        wheel1 = (SingleWheelView) findViewById(R.id.wheel_1);
        wheel1.setSlotWidth(slotWidth);
        wheel1.setSlotHeight(slotHeight);
        wheel1.setBmpList(bmpList);
        wheel1.setOnWheelStateChangedListener(new GameEngine.OnWheelStateChangedListener() {

            @Override
            public void onStop() {
                if (listener1 != null) {
                    listener1.onStop();
                }
            }

            @Override
            public void onTriggerStopSound() {
                if (listener1 != null) {
                    listener1.onTriggerStopSound();
                }
            }
        });
        wheel2 = (SingleWheelView) findViewById(R.id.wheel_2);
        wheel2.setSlotWidth(slotWidth);
        wheel2.setSlotHeight(slotHeight);
        wheel2.setBmpList(bmpList);
        wheel2.setOnWheelStateChangedListener(new GameEngine.OnWheelStateChangedListener() {

            @Override
            public void onStop() {
                if (listener2 != null) {
                    listener2.onStop();
                }
            }

            @Override
            public void onTriggerStopSound() {
                if (listener2 != null) {
                    listener2.onTriggerStopSound();
                }
            }
        });
        wheel3 = (SingleWheelView) findViewById(R.id.wheel_3);
        wheel3.setSlotWidth(slotWidth);
        wheel3.setSlotHeight(slotHeight);
        wheel3.setBmpList(bmpList);
        wheel3.setOnWheelStateChangedListener(new GameEngine.OnWheelStateChangedListener() {

            @Override
            public void onStop() {
                if (listener != null) {
                    listener.onStop();
                }
            }

            @Override
            public void onTriggerStopSound() {
                if (listener != null) {
                    listener.onTriggerStopSound();
                }
            }
        });
    }

    /**
     * 启动
     */
    public void start() {
        wheel1.start();
        postDelayed(new Runnable() {

            @Override
            public void run() {
                wheel2.start();
            }
        }, 500);
        postDelayed(new Runnable() {

            @Override
            public void run() {
                wheel3.start();
            }
        }, 1000);
    }

    /**
     * 停止信号
     *
     * @param targetPosition1
     *            滚轮1目标停止索引
     * @param targetPosition2
     *            滚轮2目标停止索引
     * @param targetPosition3
     *            滚轮3目标停止索引
     */
    public void stop(final int targetPosition1, final int targetPosition2, final int targetPosition3) {
        stop(targetPosition1, targetPosition2, targetPosition3, true);
    }

    /**
     * 停止信号
     *
     * @param targetPosition1
     *            滚轮1目标停止索引
     * @param targetPosition2
     *            滚轮2目标停止索引
     * @param targetPosition3
     *            滚轮3目标停止索引
     * @param isNeedRetry
     *            未到达最大速度，是否重试
     */
    public void stop(final int targetPosition1, final int targetPosition2, final int targetPosition3,
                     boolean isNeedRetry) {
        if (wheel1.isStartAndReachMaxVelocity() && wheel2.isStartAndReachMaxVelocity()
                && wheel3.isStartAndReachMaxVelocity()) {
            // 如果3个滚轮都在滚动状态并到达最大速度，发送停止信号
            wheel1.stop(targetPosition1);
            postDelayed(new Runnable() {

                @Override
                public void run() {
                    wheel2.stop(targetPosition2);
                }
            }, 500);
            postDelayed(new Runnable() {

                @Override
                public void run() {
                    wheel3.stop(targetPosition3);
                }
            }, 1000);
        } else {
            if (isNeedRetry) {
                // 延迟2s再重试发送停止信号
                postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        stop(targetPosition1, targetPosition2, targetPosition3, false);
                    }
                }, 3000);
            }
        }
    }

    public void startInitAnimation(final int targetPosition1, final int targetPosition2, final int targetPosition3) {

        wheel1.postDelayed(new Runnable() {

            @Override
            public void run() {
                wheel1.startInitAnimation(targetPosition1);
            }
        }, 300);
        postDelayed(new Runnable() {

            @Override
            public void run() {
                wheel2.startInitAnimation(targetPosition2);
            }
        }, 600);
        postDelayed(new Runnable() {

            @Override
            public void run() {
                wheel3.startInitAnimation(targetPosition3);
            }
        }, 900);
    }
}
