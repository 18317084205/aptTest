package com.liang.annotations;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.swing.text.View;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@ListenerClass(
        targetType = "onClick",
        setter = "setOnClick",
        parameters = "android.view.View",
        getAnnotationClass = BindView.class
)
public @interface OnClick {
    @IdRes int[] value() default {-1};
}
