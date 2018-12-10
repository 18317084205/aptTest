package com.liang.inject;

import android.support.annotation.IdRes;
import android.view.View;

public class ViewUtils {
    public static <T extends View> T findViewAsType(View view, @IdRes int id) {
        return (T) view.findViewById(id);//强转
    }
}
