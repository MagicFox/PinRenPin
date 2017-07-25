package com.fox.pinrenpin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.fox.pinrenpin.R;
import com.fox.pinrenpin.utils.ViewUtil;

import java.util.HashMap;

/**
 * Created by MagicFox on 2016/8/18.
 */
public class DigitalImageEdittText extends EditText {

    private HashMap<Integer, Integer> hashMap;
    private boolean isNeedHandle = false;
    private int selectPostion = 0;
    private boolean selectFlag = true;
    private static final int MIN_NUM = 50;
    private static final int MAX_NUM = 100000;
    private Context context;

    public DigitalImageEdittText(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DigitalImageEdittText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @SuppressLint("UseSparseArrays")
    private void init() {
        ViewUtil.disableCopyPasteForEditText(this);

        setInputType(InputType.TYPE_CLASS_NUMBER);

        hashMap = new HashMap<>();
        hashMap.put(0, R.drawable.laba_entercoins_number_0);
        hashMap.put(1, R.drawable.laba_entercoins_number_1);
        hashMap.put(2, R.drawable.laba_entercoins_number_2);
        hashMap.put(3, R.drawable.laba_entercoins_number_3);
        hashMap.put(4, R.drawable.laba_entercoins_number_4);
        hashMap.put(5, R.drawable.laba_entercoins_number_5);
        hashMap.put(6, R.drawable.laba_entercoins_number_6);
        hashMap.put(7, R.drawable.laba_entercoins_number_7);
        hashMap.put(8, R.drawable.laba_entercoins_number_8);
        hashMap.put(9, R.drawable.laba_entercoins_number_9);

        isNeedHandle = true;
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (selectFlag) {
                    selectPostion = s.length() - getSelectionStart();
                    selectFlag = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isNeedHandle) {
                    isNeedHandle = false;

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < editable.length(); i++) {
                        char c = editable.charAt(i);
                        if (c >= '0' && c <= '9') {
                            sb.append(c);
                        }
                    }

                    String result = sb.toString();
                    int number = Integer.valueOf(result);
                    if (number >= MAX_NUM) {
                        result = String.valueOf(MAX_NUM);
                    } else if (number <= MIN_NUM) {
                        result = String.valueOf(MIN_NUM);
                    }

                    SpannableStringBuilder ssb = new SpannableStringBuilder(result);
                    for (int i = 0; i < result.length(); i++) {
                        char c = result.charAt(i);
                        if (c >= '0' && c <= '9') {
                            ssb.setSpan(new ImageSpan(getContext(), hashMap.get(c - '0')), i, i + 1,
                                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    }
                    setText(ssb);
                    try {
                        setSelection(ssb.length() - selectPostion);
                    } catch (Exception e) {
                        setSelection(0);
                    }

                    selectFlag = true;
                } else {
                    isNeedHandle = true;
                }
            }
        });

        setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setCursorVisible(hasFocus);
            }
        });

    }

}
