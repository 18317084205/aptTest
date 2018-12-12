package com.liang.inject;

import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ViewUtils {
    private static boolean enabled = true;
    private static final Runnable ENABLE_AGAIN = () -> enabled = true;

    public static <T extends View> T findViewAsType(View view, @IdRes int id) {
        return (T) view.findViewById(id);//强转
    }

    public static void setOnClick(View view, @IdRes int id, ViewListener listener) {
        view.findViewById(id).setOnClickListener(v -> {
            if (enabled) {
                enabled = false;
                v.post(ENABLE_AGAIN);
                if (listener != null) {
                    listener.onClick(v);
                }

            }
        });
    }

    public static void setOnLongClick(View view, @IdRes int id, ViewListener listener) {
        view.findViewById(id).setOnLongClickListener(v -> {
            if (listener != null) {
                return listener.onLongClick(v);
            }
            return false;
        });
    }

    public static void setOnCheckedChange(View view, @IdRes int id, ViewListener listener) {
        View v = view.findViewById(id);
        if (v instanceof CompoundButton) {
            ((CompoundButton) v).setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onCheckedChanged(buttonView, isChecked);
                }
            });
        }
        if (v instanceof RadioGroup) {
            ((RadioGroup) v).setOnCheckedChangeListener((group, checkedId) -> {
                if (listener != null) {
                    listener.onCheckedChanged(group, checkedId);
                }
            });
        }
    }

    public static void addTextChanged(View view, @IdRes int id, ViewListener listener) {
        View v = view.findViewById(id);
        if (v instanceof TextView) {
            ((TextView) v).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (listener != null) {
                        listener.onTextChanged(s, start, before, count);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    public static void setOnEditorAction(View view, @IdRes int id, ViewListener listener) {
        View v = view.findViewById(id);
        if (v instanceof TextView) {
            ((TextView) v).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (listener != null) {
                        listener.onEditorAction(v, actionId, event);
                    }
                    return false;
                }
            });
        }
    }

}
