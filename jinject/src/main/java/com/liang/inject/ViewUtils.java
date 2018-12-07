package com.liang.inject;

import android.support.annotation.IdRes;
import android.view.View;

public class ViewUtils {
    public static <T> T findViewAsType(View source, @IdRes int id, Class<T> cls) {
        View view = findRequiredView(source, id); //找到view
        return cls.cast(view) ;//强转 如(TexteView)mTextView之类
    }

    public static View findRequiredView(View source, @IdRes int id) {
        View view = source.findViewById(id); //从decoreView中执行findViewById
        return view;
    }
}
