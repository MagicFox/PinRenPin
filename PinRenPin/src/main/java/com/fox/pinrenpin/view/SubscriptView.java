package com.fox.pinrenpin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.fox.pinrenpin.R;

/**
 * Created by MagicFox on 2016/8/18.
 */
public class SubscriptView extends ImageView {

    public SubscriptView(Context context) {
        super(context);
        init();
    }

    public SubscriptView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setVisibility(View.GONE);
    }

    public void setCount(int count) {
        if (count <= 0) {
            setVisibility(View.GONE);
            return;
        } else if (count >= 10) {
            setImageResource(R.drawable.laba_subscript_more);
            setVisibility(View.VISIBLE);
            return;
        }

        setVisibility(View.VISIBLE);
        switch (count) {
            case 1:
                setImageResource(R.drawable.laba_subscript_1);
                break;
            case 2:
                setImageResource(R.drawable.laba_subscript_2);
                break;
            case 3:
                setImageResource(R.drawable.laba_subscript_3);
                break;
            case 4:
                setImageResource(R.drawable.laba_subscript_4);
                break;
            case 5:
                setImageResource(R.drawable.laba_subscript_5);
                break;
            case 6:
                setImageResource(R.drawable.laba_subscript_6);
                break;
            case 7:
                setImageResource(R.drawable.laba_subscript_7);
                break;
            case 8:
                setImageResource(R.drawable.laba_subscript_8);
                break;
            case 9:
                setImageResource(R.drawable.laba_subscript_9);
                break;
            default:
                setVisibility(View.GONE);
                break;
        }
    }
}

