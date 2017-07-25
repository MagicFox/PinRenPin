package com.fox.pinrenpin.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fox.pinrenpin.R;

/**
 * Created by MagicFox on 2016/8/19.
 */
public class CustomDialog extends Dialog {
    private int default_width = 278; // 默认宽度
    private int default_height = 223;// 默认高度
    boolean hasTwoButton = false;
    android.view.View.OnClickListener confirmBtnListener;
    android.view.View.OnClickListener LeftBtnListener;
    android.view.View.OnClickListener rightBtnListener;

    private View threeBottonLv;
    private View three_leftBtn;
    private View three_midBtn;
    private View three_rightBtn;
    private View twoBottonLv;
    private View leftBtn;
    private View rightBtn;
    private View confirmBtn;
    private View messageTv;
//    private View titleTv;

    String message;

    public CustomDialog(Context context, int layout, int style, boolean hasTwoButton) {
        super(context, style);
        setContentView(layout);
        twoBottonLv = findViewById(R.id.twoBottonLv);
        leftBtn = findViewById(R.id.leftBtn);
        rightBtn = findViewById(R.id.rightBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        messageTv = findViewById(R.id.messageTv);
        // 兼容万里通对话框样式
//        try {
//            titleTv = findViewById(R.id.titleTv);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (hasTwoButton) {
            twoBottonLv.setVisibility(View.VISIBLE);
            confirmBtn.setVisibility(View.GONE);
        } else {
            twoBottonLv.setVisibility(View.GONE);
            confirmBtn.setVisibility(View.VISIBLE);
        }

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        float density = getDensity(context);
        params.width = (int) (default_width * density);
        params.height = (int) (default_height * density);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    public CustomDialog(Context context, int layout, int style, boolean hasTwoButton, boolean hasThreeButton) {
        super(context, style);
        setContentView(layout);
        threeBottonLv = findViewById(R.id.threeBottonLv);
        three_leftBtn = findViewById(R.id.three_leftBtn);
        three_midBtn = findViewById(R.id.three_midBtn);
        three_rightBtn = findViewById(R.id.three_rightBtn);
        twoBottonLv = findViewById(R.id.twoBottonLv);
        leftBtn = findViewById(R.id.leftBtn);
        rightBtn = findViewById(R.id.rightBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        messageTv = findViewById(R.id.messageTv);
        // 兼容万里通对话框样式
//        try {
//            titleTv = findViewById(R.id.titleTv);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (hasThreeButton) {
            threeBottonLv.setVisibility(View.VISIBLE);
            twoBottonLv.setVisibility(View.GONE);
            confirmBtn.setVisibility(View.GONE);
        } else {
            threeBottonLv.setVisibility(View.GONE);
            if (hasTwoButton) {
                twoBottonLv.setVisibility(View.VISIBLE);
                confirmBtn.setVisibility(View.GONE);
            } else {
                twoBottonLv.setVisibility(View.GONE);
                confirmBtn.setVisibility(View.VISIBLE);
            }
        }

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        float density = getDensity(context);
        params.width = (int) (default_width * density);
        params.height = (int) (default_height * density);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    private float getDensity(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.density;
    }

    /** 设置Dialog宽度 */
    public void setWidth(int width) {
        default_width = width;
    };

    /** 设置Dialog宽度 */
    // public void setHeight(int height) {
    // default_height = height;
    // };

    /**
     * 当dialog上有两个按钮时，设置左边按钮的点击事件
     *
     * @param listener
     *            点击时间监听器
     */
    public void setLeftButtonListener(android.view.View.OnClickListener listener) {
        leftBtn.setOnClickListener(listener);
    }

    /**
     * 当dialog上有两个按钮时，设置右边边按钮的点击事件
     *
     * @param listener
     *            点击时间监听器
     */
    public void setRightButtonListener(android.view.View.OnClickListener listener) {
        rightBtn.setOnClickListener(listener);
    }

    /**
     * 当dialog上有三个个按钮时，设置按钮的点击事件
     */
    public void setThreeButtonListener(android.view.View.OnClickListener listener_left,
                                       android.view.View.OnClickListener listener_mid, android.view.View.OnClickListener listener_right) {
        three_leftBtn.setOnClickListener(listener_left);
        three_midBtn.setOnClickListener(listener_mid);
        three_rightBtn.setOnClickListener(listener_right);
    }

    /**
     * 当dialog上只有一个按钮时，设置按钮的点击事件
     *
     * @param listener
     *            点击时间监听器
     */
    public void setConfirmListener(android.view.View.OnClickListener listener) {
        confirmBtn.setOnClickListener(listener);
    }

    /**
     * 当dialog按钮只有一个时，设置按扭标题
     *
     * @param title
     *            按钮标题
     */
    public void setConfirmButtonText(String title) {
        ((Button) confirmBtn).setText(title);
    }

    /**
     * 当dialog按钮两个时，设置左边按扭标题
     *
     * @param title
     *            按钮标题
     */
    public void setLeftButtonText(String title) {
        ((Button) leftBtn).setText(title);
    }

    /**
     * 当dialog按钮有两个时，设置按扭标题
     *
     * @param title
     *            按钮标题
     */
    public void setRightButtonText(String title) {
        ((Button) rightBtn).setText(title);
    }

    /**
     * 当dialog按钮有三个时，设置按扭标题
     */
    public void setThreeButtonText(String left, String mid, String right) {
        ((Button) three_leftBtn).setText(left);
        ((Button) three_midBtn).setText(mid);
        ((Button) three_rightBtn).setText(right);
    }

    /**
     * 设置提示信息
     *
     * @param message
     *            按钮标题
     */
    public void setMessage(String message) {
        ((TextView) messageTv).setText(message);
    }

//    public void setTitle(String title) {
//        ((TextView) titleTv).setText(title);
//    }

    /**
     * 设置 Gravity {@link Gravity}
     */
    public void setMessageTvGravity(int gravity) {
        ((TextView) messageTv).setGravity(gravity);
    }

}
