package com.fox.pinrenpin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fox.pinrenpin.utils.CommonHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MagicFox on 2016/8/18.
 */
public abstract class GameEngine extends ImageView {

    /** 1秒 */
    private final static int ONE_SECOND = 1000;

    /**
     * 刷新间隔，单位：ms. 间隔尽量能整除1s，以保证帧数是整数
     * */
    private final static int INTERVAL = 20;

    /** 加速度，单位：px/s^2 */
    private static int ACCELERATION;

    /** 最大速度，单位：px/s */
    private static int MAX_VELOCITY;

    /** 每帧最大速度，单位：px/frame */
    private static int MAX_VELOCITY_PER_TICK;

    /** 调用stop后，到静止时间 */
    private static final int STOP_TIME = 2000;

    /** 速度，单位：px/s */
    private int velocity = 0;

    /** 槽宽 */
    protected int slotWidth;
    /** 槽高 */
    protected int slotHeight;
    /** 总槽高， 相当于所有图标总高 */
    protected int slotTotalHeight;

    /* 摄像机位置 */
    private int cameraY = 0;

    /** 停止信号 */
    private boolean stopSignal = true;
    /** 到静止前，还要滚动多少距离 */
    private int stopRemainOffset = 0;
    /** 到静止前，还有多少帧数 */
    private int stopTickCount = STOP_TIME / INTERVAL;

    /** 播放声音时机，离静止前的帧数. 声音长度为240ms，所以为12帧 */
    private static final int PLAY_STOP_SOUND_TICK = 12;

    /** 锁 */
    private Object lock = new Object();

    /** 计时器 */
    private Timer timer;

    public static interface OnWheelStateChangedListener {
        void onStop();

        void onTriggerStopSound();
    }

    private OnWheelStateChangedListener listener;

    public void setOnWheelStateChangedListener(OnWheelStateChangedListener listener) {
        this.listener = listener;
    }

    public GameEngine(Context context) {
        super(context);
        init();
    }

    public GameEngine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ACCELERATION = (int) CommonHelper.dipToPixel(-250);
        MAX_VELOCITY = (int) CommonHelper.dipToPixel(-1000);
        MAX_VELOCITY_PER_TICK = MAX_VELOCITY * INTERVAL / ONE_SECOND;
    }

    public void setSlotWidth(int slotWidth) {
        this.slotWidth = slotWidth;
    }

    public void setSlotHeight(int slotHeight) {
        this.slotHeight = slotHeight;
        slotTotalHeight = slotHeight * 6;
    }

    public void start() {
        velocity = 0;
        stopSignal = false;
        if (timer == null) {
            timer = new Timer();
            TimerTask timerTastk = new TimerTask() {

                @Override
                public void run() {
                    synchronized (lock) {
                        if (stopSignal) {
                            // 停止中
                            stopTickCount--;

                            if (stopTickCount == PLAY_STOP_SOUND_TICK) {
                                if (listener != null) {
                                    listener.onTriggerStopSound();
                                }
                            }

                            if (stopTickCount <= 0) {
                                timer.cancel();
                                timer = null;

                                if (listener != null) {
                                    listener.onStop();
                                }
                                return;
                            }

                            int currentV = 0;
                            if (stopTickCount == 1) {
                                // 最后一帧，速度等于剩余偏移量
                                currentV = stopRemainOffset;
                            } else {
                                // 每一帧都重新构建等差数列并计算速度
                                currentV = stopRemainOffset * 2 / stopTickCount;
                            }

                            if (currentV < MAX_VELOCITY_PER_TICK) {
                                // 控制在最大速度以内，平滑过渡
                                currentV = MAX_VELOCITY_PER_TICK;
                            }
                            if (currentV > 0) {
                                timer.cancel();
                                timer = null;

                                if (listener != null) {
                                    listener.onStop();
                                }
                                return;
                            }
                            cameraY += currentV;
                            stopRemainOffset -= currentV;
                            onCameraYChanged(cameraY);
                        } else {
                            // 开始或滚动中
                            velocity += (ACCELERATION / INTERVAL);
                            if (Math.abs(velocity) > Math.abs(MAX_VELOCITY)) {
                                velocity = MAX_VELOCITY;
                            }
                            cameraY += (velocity * INTERVAL / ONE_SECOND);
                            onCameraYChanged(cameraY);
                        }
                    }
                }
            };
            timer.schedule(timerTastk, 0, INTERVAL);
        }
    }

    /**
     * 停止信号
     *
     * 从接收到信号开始，每一帧的速度，为当前所剩帧数计算出的登出数列的最后一项。 每一帧都重新构建等差数列并计算速度。
     *
     * @param targetPosition
     *            目标位置索引
     */
    public void stop(final int targetPosition) {
        synchronized (lock) {
            int adjustOffset = cameraY % slotTotalHeight;
            int tragetOffset = getOffset(targetPosition);
            int tempOffset = (tragetOffset - adjustOffset) % slotTotalHeight;

            // 保证到真正静止前，转圈数为2圈多
            if (tempOffset > 0) {
                tempOffset -= slotTotalHeight;
            }
            int totalOffset = tempOffset - slotTotalHeight * 2;

            stopTickCount = STOP_TIME / INTERVAL;
            stopRemainOffset = totalOffset;
            stopSignal = true;
        }
    }

    protected void onCameraYChanged(int cameraY) {
        this.cameraY = cameraY;
    }

    /**
     * @param position
     *            索引位置
     * @return cameraY 相对起点偏移
     */
    private int getOffset(int position) {
        return slotHeight * position + (slotHeight / 2 - getHeight() / 2);
    }

    public boolean isStartAndReachMaxVelocity() {
        return !stopSignal && Math.abs(velocity) == Math.abs(MAX_VELOCITY);
    }

    // ========== 初始动画 ==========//
    /** 到静止前，还有多少帧数 */
    private int initAnimCurrentTick = INIT_ANIM_TIME / INTERVAL;
    /** 到静止前，还要滚动多少距离 */
    private int initAnimStopRemainOffset = 0;
    /** 动画时间 */
    private static final int INIT_ANIM_TIME = 500;

    public void startInitAnimation(int targetPosition) {
        synchronized (lock) {
            int adjustOffset = cameraY % slotTotalHeight;
            int tragetOffset = getOffset(targetPosition);
            int tempOffset = (tragetOffset - adjustOffset) % slotTotalHeight;
            if (tempOffset > 0) {
                // 保证向下滚动
                tempOffset -= slotTotalHeight;
            }

            int totalOffset = tempOffset;
            initAnimCurrentTick = INIT_ANIM_TIME / INTERVAL;
            initAnimStopRemainOffset = totalOffset;
        }

        if (timer == null) {
            timer = new Timer();
            TimerTask timerTastk = new TimerTask() {

                @Override
                public void run() {
                    synchronized (lock) {
                        initAnimCurrentTick--;

                        if (initAnimCurrentTick == PLAY_STOP_SOUND_TICK) {
                            if (listener != null) {
                                listener.onTriggerStopSound();
                            }
                        }

                        if (initAnimCurrentTick <= 0) {
                            timer.cancel();
                            timer = null;

                            if (listener != null) {
                                listener.onStop();
                            }
                            return;
                        }
                        int currentV = initAnimStopRemainOffset / initAnimCurrentTick;
                        if (currentV > 0) {
                            timer.cancel();
                            timer = null;

                            if (listener != null) {
                                listener.onStop();
                            }
                            return;
                        }
                        cameraY += currentV;
                        initAnimStopRemainOffset -= currentV;
                        onCameraYChanged(cameraY);
                    }
                }
            };
            timer.schedule(timerTastk, 0, INTERVAL);
        }
    }
}
