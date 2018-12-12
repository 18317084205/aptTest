package com.liang.complier;

import com.liang.annotations.BindView;
import com.liang.annotations.OnCheckedChange;
import com.liang.annotations.OnClick;
import com.liang.annotations.OnEditorAction;
import com.liang.annotations.OnLongClick;
import com.liang.annotations.OnTextChanged;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Containers {

    public static final ClassName UNBIND = ClassName.get("com.liang.inject", "UnBinder");
    public static final ClassName VIEW = ClassName.get("android.view", "View");
    public static final ClassName CONTEXT = ClassName.get("android.content", "Context");
    public static final ClassName RESOURCES = ClassName.get("android.content.res", "Resources");
    public static final ClassName VIEW_UTILS = ClassName.get("com.liang.inject", "ViewUtils");
    public static final ClassName VIEW_LISTENER = ClassName.get("com.liang.inject", "ViewListener");

    public static final String CONSTRUCTOR = "<init>";
    public static final String INJECTOR = "$$Injector";
    public static final String METHOD_UNBIND = "unbind";
    public static final String METHOD_SET_LISTENER = "setViewListener";
    public static final String METHOD_CLICK = "OnClick";
    public static final String METHOD_LONG_CLICK = "OnLongClick";



    public static Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(BindView.class);
        annotations.add(OnClick.class);
        annotations.add(OnLongClick.class);
        annotations.add(OnCheckedChange.class);
        annotations.add(OnEditorAction.class);
        annotations.add(OnTextChanged.class);
        return annotations;
    }

    public static TypeName getTypeName(String returnType) {
        switch (returnType) {
            case "void":
                return TypeName.VOID;
            case "boolean":
                return TypeName.BOOLEAN;
            case "byte":
                return TypeName.BYTE;
            case "char":
                return TypeName.CHAR;
            case "double":
                return TypeName.DOUBLE;
            case "float":
                return TypeName.FLOAT;
            case "int":
                return TypeName.INT;
            case "long":
                return TypeName.LONG;
            case "short":
                return TypeName.SHORT;
            default:
                return TypeName.VOID;
        }
    }
}
