package com.liang.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ListenerClass {
    String targetType();
    String setter();
    String[] parameters() default { };
    String returnType() default "void";
    String defaultReturn() default "null";
    Class<? extends Annotation> getAnnotationClass();
}
