package com.fox.pinrenpin.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * Created by MagicFox on 2016/8/19.
 */
public class ViewUtil {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void disableCopyPasteForEditText(EditText editText) {
        if (editText == null) {
            return;
        }
        editText.setLongClickable(false);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            editText.setTextIsSelectable(false);
            editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }
            });
        }
    }
}
