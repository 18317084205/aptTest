package com.liang.lib;

import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {
    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类  许多元素
    private Messager mMessager; //日志相关的辅助类
    private Map<String, AnnotatedClass> mAnnotatedClassMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mAnnotatedClassMap = new TreeMap<>();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationType = new LinkedHashSet<>();
        annotationType.add(TypeUtil.ANNOTATION_PATH);
        return annotationType;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    //这个方法是核心方法，在这里处理的你的业务。检测类别参数，生成java文件等
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mMessager.printMessage(Diagnostic.Kind.NOTE,"process...");
        mAnnotatedClassMap.clear();
        try {
            processActivityCheck(roundEnvironment);
        } catch (Exception e) {
            e.printStackTrace();
            error(e.getMessage());
        }

        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                annotatedClass.generateActivityFile().writeTo(mFiler);
            } catch (Exception e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }
        return true;
    }

    private void processActivityCheck(RoundEnvironment roundEnv) throws IllegalArgumentException, ClassNotFoundException {
        //获取所有标注了MyClass的元素
        for (Element element : roundEnv.getElementsAnnotatedWith((Class<? extends Annotation>) Class.forName(TypeUtil.ANNOTATION_PATH))) {
            if (element.getKind() == ElementKind.CLASS) {
                getAnnotatedClass(element);
            } else
                error("ActivityInject only can use  in ElementKind.CLASS");
        }
    }

    private AnnotatedClass getAnnotatedClass(Element element) {
        // tipe . can not use chines  so  ....
        // get TypeElement  element is class's --->class  TypeElement typeElement = (TypeElement) element
        //  get TypeElement  element is method's ---> TypeElement typeElement = (TypeElement) element.getEnclosingElement();

//        //获取变量比如(button,textview...)
//        VariableElement variableElement = (VariableElement) element;
//        //获取变量所在的类(比如paic.com.annotation.ManinActivity)
//        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
//        //获取类名全称
//        String fqClassName = typeElement.getQualifiedName().toString();

        TypeElement typeElement = (TypeElement) element;
        String fullName = typeElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(typeElement, mElementUtils, mMessager);
            mAnnotatedClassMap.put(fullName, annotatedClass);
        }
        return annotatedClass;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void log(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
