package com.liang.complier;

import com.liang.annotations.BindView;
import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Containers {

    public static final String METHOD_UNBIND = "unbind";
    public static final String INJECTOR = "$$Injector";
    public static final ClassName UNBIND = ClassName.get("com.liang.inject", "UnBinder");
    public static final ClassName VIEW = ClassName.get("android.view", "View");
    public static final ClassName CONTEXT = ClassName.get("android.content", "Context");
    public static final ClassName RESOURCES = ClassName.get("android.content.res", "Resources");
    public static final ClassName UTILS = ClassName.get("butterknife.internal", "Utils");

    public static Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(BindView.class);
        return annotations;
    }
}
