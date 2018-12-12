package com.liang.complier;


import com.liang.annotations.ListenerClass;
import com.liang.annotations.OnClick;
import com.liang.annotations.OnLongClick;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class AnnotatedClass {

    private final String parameterName;
    private TypeElement typeElement;
    private Elements elements;
    private List<Element> elementList;
    private Map<ListenerClass, List<MethodViewBinding>> listenerClassListMap;
    private boolean isViewListener;

    public AnnotatedClass(TypeElement typeElement, Elements elements) {
        this.typeElement = typeElement;
        this.elements = elements;
        elementList = new ArrayList<>();
        listenerClassListMap = new HashMap<>();
        parameterName = toLowerCaseFirstOne(typeElement.getSimpleName() + "");
    }

    public void addElement(Element element) {
        elementList.add(element);
    }

    public void addElement(ListenerClass key, MethodViewBinding viewBinding) {
        List<MethodViewBinding> elements = listenerClassListMap.get(key);
        if (elements == null) {
            elements = new ArrayList<>();
            listenerClassListMap.put(key, elements);
        }
        elements.add(viewBinding);
    }

    public JavaFile generateActivityFile() {
        // build inject method
        MethodSpec.Builder constructorMethod = getMethodSpecBuilder(Containers.CONSTRUCTOR, false);
        constructorMethod.addParameter(TypeName.get(typeElement.asType()), parameterName);
        constructorMethod.addParameter(Containers.VIEW, "view");
        constructorMethod.addStatement("this.$L = $L", parameterName, parameterName);
        constructorMethod.addStatement("this.$L = $L", "view", "view");

        addConstructorSource(constructorMethod);

        MethodSpec.Builder unBindMethod = getMethodSpecBuilder(Containers.METHOD_UNBIND, true);
        addUnBingMethodCode(unBindMethod);

        TypeSpec.Builder injectClass = getTypeSpecBuilder(typeElement.getSimpleName() + Containers.INJECTOR);
        injectClass.addMethod(constructorMethod.build());
        injectClass.addMethod(unBindMethod.build());

        addBingListenerMethodCode(injectClass);

        String packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();
        return JavaFile.builder(packageName, injectClass.build()).build();
    }

    private void addConstructorSource(MethodSpec.Builder methodSpec) {
        for (ListenerClass listenerClass : listenerClassListMap.keySet()) {
            if (listenerClass.targetType().equals("bindView")) {
                createBingViewCode(methodSpec, listenerClassListMap.get(listenerClass));
                continue;
            }
            isViewListener = true;
        }

        if (!isViewListener) {
            return;
        }

        methodSpec.addStatement("$L(this)", Containers.METHOD_SET_LISTENER);
    }

    private void createBingViewCode(MethodSpec.Builder methodSpec, List<MethodViewBinding> methodViewBindings) {
        for (MethodViewBinding viewBinding : methodViewBindings) {
            CodeBlock.Builder builder = createBingFieldCode(viewBinding);
            methodSpec.addStatement("$L", builder.build());
        }
    }

    private CodeBlock.Builder createBingFieldCode(MethodViewBinding viewBinding) {
        CodeBlock.Builder builder = CodeBlock.builder()
                .add("$L.$L = ", parameterName, viewBinding.getName());
        builder.add("$T.findViewAsType(view,$L)", Containers.VIEW_UTILS, viewBinding.getIds()[0]);
        return builder;
    }

    private void addUnBingMethodCode(MethodSpec.Builder methodSpec) {
        methodSpec.addStatement("$T target = $L", TypeName.get(typeElement.asType()), parameterName);
        for (Element element : elementList) {
            if (element.getKind() == ElementKind.FIELD) {
                CodeBlock.Builder builder = createUnBingFieldCode(element);
                methodSpec.addStatement("$L", builder.build());
            }
        }
        createUnBingMethodCode(methodSpec);
    }

    private CodeBlock.Builder createUnBingFieldCode(Element element) {
        CodeBlock.Builder builder = CodeBlock.builder()
                .add("$L.$L = null", parameterName, element.getSimpleName().toString());
        return builder;
    }

    private CodeBlock.Builder createUnBingMethodCode(MethodSpec.Builder methodSpec) {
        CodeBlock.Builder builder = CodeBlock.builder();
        methodSpec.addStatement("$L(null)", Containers.METHOD_SET_LISTENER);
        return builder;
    }

    private void addBingListenerMethodCode(TypeSpec.Builder injectClass) {
        if (!isViewListener) {
            return;
        }

        addListenerMethod(injectClass);

        MethodSpec.Builder setListenerMethod = getMethodSpecBuilder(Containers.METHOD_SET_LISTENER, false);
        setListenerMethod.addParameter(Containers.VIEW_LISTENER, "listener");
//
//
//        MethodSpec.Builder onClick = getMethodSpecBuilder(Containers.METHOD_CLICK, true);
//        onClick.addParameter(Containers.VIEW, "v");
//        onClick.addStatement("$L", createClickMethodCode(elementList).build());
//
//        MethodSpec.Builder onLongClick = getMethodSpecBuilder(Containers.METHOD_LONG_CLICK, true);
//        onLongClick.returns(TypeName.BOOLEAN);
//        onLongClick.addParameter(Containers.VIEW, "v");
//        onLongClick.addStatement("$L", createLongClickMethodCode(elementList).build());


        injectClass.superclass(Containers.VIEW_LISTENER);
        injectClass.addMethod(setListenerMethod.build());
    }

    private void addListenerMethod(TypeSpec.Builder builder) {
        for (Map.Entry<ListenerClass, List<MethodViewBinding>> entry : listenerClassListMap.entrySet()) {
            MethodSpec.Builder listenerMethod = createBingListenerMethod(entry.getKey());
            listenerMethod.addStatement("$L", createListenerMethodCode(entry.getKey(), entry.getValue()));
            builder.addMethod(listenerMethod.build());
        }
//
//
//        for (Element element : elementList) {
//            if (element.getKind() == ElementKind.METHOD) {
//                createBingListenerMethodCode(builder, element);
//            }
//        }
    }

    private MethodSpec.Builder createBingListenerMethod(ListenerClass listenerClass) {
        MethodSpec.Builder builder = getMethodSpecBuilder(listenerClass.targetType(), true);
        builder.returns(Containers.getTypeName(listenerClass.returnType()));
        String[] parameterTypes = listenerClass.parameters();
        for (String parameterType : parameterTypes) {
            builder.addParameter(Containers.getTypeName(parameterType),
                    "parameter" + Arrays.binarySearch(parameterTypes, parameterType));
        }
        return builder;
    }

    private CodeBlock.Builder createListenerMethodCode(ListenerClass listenerClass, List<MethodViewBinding> methodViewBindings) {
        CodeBlock.Builder builder = CodeBlock.builder().beginControlFlow("switch(v.getId())");
        for (MethodViewBinding viewBinding : methodViewBindings) {
            for (int value : viewBinding.getIds()) {
                builder.add("case $L:\n", value);
            }
            if (listenerClass.returnType().equals("void")) {
                builder.add("$L.$L();\n", parameterName, viewBinding.getName());
                builder.add("break;\n");
            } else {
                builder.add("return $L.$L();\n", parameterName, viewBinding.getName());
            }
        }
        builder.endControlFlow();
        return builder;
    }

    private CodeBlock.Builder createLongClickMethodCode(List<Element> elementList) {
        CodeBlock.Builder builder = CodeBlock.builder().beginControlFlow("switch(v.getId())");
        for (Element element : elementList) {
            OnLongClick longClick = element.getAnnotation(OnLongClick.class);
            if (null == longClick) {
                continue;
            }
            for (int value : longClick.value()) {
                builder.add("case $L:\n", value);
            }
            builder.add("return $L.$L();\n", parameterName, element.getSimpleName().toString());
        }
        builder.endControlFlow();
        builder.add("return false");
        return builder;
    }

    private TypeSpec.Builder getTypeSpecBuilder(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Containers.UNBIND)
                .addField(TypeName.get(typeElement.asType()), parameterName, Modifier.PRIVATE)
                .addField(Containers.VIEW, "view", Modifier.PRIVATE);
    }

    private MethodSpec.Builder getMethodSpecBuilder(String name, boolean isOverride) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC);
        if (isOverride) {
            builder.addAnnotation(Override.class);
        }
        return builder;
    }


    private void createBingListenerMethodCode(MethodSpec.Builder methodSpec, Element element) {
        OnClick onClick = element.getAnnotation(OnClick.class);
        OnLongClick longClick = element.getAnnotation(OnLongClick.class);
        if (onClick != null) {
            for (int value : onClick.value()) {
                methodSpec.addStatement("$T.setOnClick(view,$L,listener)", Containers.VIEW_UTILS, value);
            }
        }

        if (longClick != null) {
            for (int value : longClick.value()) {
                methodSpec.addStatement("$T.setOnLongClick(view,$L,listener)", Containers.VIEW_UTILS, value);
            }
        }

    }


    private String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }


}
