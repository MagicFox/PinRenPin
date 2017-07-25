package com.fox.pinrenpin.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fox.pinrenpin.R;
import com.fox.pinrenpin.view.base.LabaBaseDialog;

/**
 * Created by MagicFox on 2016/8/19.
 */
public class LabaCoinRewardDialog extends LabaBaseDialog<String> {
    public static interface OnGivePointBtnClickListener {
        void GivePointBtnClick(String earnPoints);
    }

    private OnGivePointBtnClickListener listener;

    public LabaCoinRewardDialog(Context context) {
        super(context);
    }

    public LabaCoinRewardDialog(Context context, int layoutId) {
        super(context, layoutId);
    }

    public void setOnGivePointBtnClickListener(OnGivePointBtnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void setUIDataAndShow(final String response) {
        View v = getRootView();
        TextView scoreNum = (TextView) v.findViewById(R.id.reward_num);
        Button getRewardBtn = (Button) v.findViewById(R.id.get_reward_btn);
        getRewardBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.GivePointBtnClick(response);
                }
                LabaCoinRewardDialog.this.dismiss();
            }
        });
        scoreNum.setText(response);
        LabaCoinRewardDialog.this.show();

    }

}
