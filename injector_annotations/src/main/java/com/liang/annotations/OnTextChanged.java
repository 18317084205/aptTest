package com.liang.annotations;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@ListenerClass(
        targetType = "onTextChanged",
        setter = "setOnTextChanged",
        parameters = {
                "java.lang.CharSequence",
                "int",
                "int",
                "int"
        },
        getAnnotationClass = BindView.class
)
public @interface OnTextChanged {
    @IdRes int[] value() default {-1};
}
