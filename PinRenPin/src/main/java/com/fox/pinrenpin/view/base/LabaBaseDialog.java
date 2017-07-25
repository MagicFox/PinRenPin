package com.fox.pinrenpin.view.base;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.fox.pinrenpin.R;

/**
 * Created by MagicFox on 2016/8/19.
 */
public abstract class LabaBaseDialog<T> extends Dialog {
    private Context context;
    private View mRoot;
    private int layoutId;

    public LabaBaseDialog(Context context) {
        super(context);
        this.context = context;
        initDialog();
    }

    public LabaBaseDialog(Context context, int layoutId) {
        super(context, R.style.Dialog_Fullscreen);
        this.context = context;
        this.layoutId = layoutId;
        initDialog();
    }

    private View getView() {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    public View getRootView() {
        if (mRoot != null) {
            return mRoot;
        }
        return null;
    }

    public abstract void setUIDataAndShow(T response);

    private void initDialog() {
        if (mRoot == null) {
            mRoot = getView();
            setContentView(mRoot);
        }
        // mRoot.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // dismiss();
        // }
        // });

    }
}
