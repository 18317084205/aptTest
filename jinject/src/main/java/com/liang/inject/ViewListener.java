package com.liang.inject;

import android.view.KeyEvent;
import android.view.View;

public abstract class ViewListener {
    public void onClick(View v) {
    }

    public boolean onLongClick(View v) {
        return false;
    }

    public void onCheckedChanged(View v, boolean isChecked) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void onCheckedChanged(View v, int checkedId) {
    }

    public void onEditorAction(View v, int actionId, KeyEvent event) {
    }
}
