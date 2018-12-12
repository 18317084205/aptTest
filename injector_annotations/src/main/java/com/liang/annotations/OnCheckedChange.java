package com.liang.annotations;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@ListenerClass(
        targetType = "onCheckedChange",
        setter = "setOnCheckedChange",
        parameters = {
                "android.view.View",
                "boolean"
        }, getAnnotationClass = BindView.class
)
public @interface OnCheckedChange {
    @IdRes int[] value() default {-1};
}
