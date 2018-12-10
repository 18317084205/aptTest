package com.liang.complier;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class AnnotatedClass {

    private TypeElement typeElement;
    private Elements elements;
    private List<Element> elementList;

    public AnnotatedClass(TypeElement typeElement, Elements elements) {
        this.typeElement = typeElement;
        this.elements = elements;
//        elementList = (List<Element>) elements.getAllMembers(typeElement);
    }

    public JavaFile generateActivityFile() {
        // build inject method

        MethodSpec.Builder initMethod = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(typeElement.asType()), "activity")
                .addStatement("this(activity, activity.getWindow().getDecorView())");

        MethodSpec.Builder init2Method = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(typeElement.asType()), "activity")
                .addParameter(Containers.VIEW, "source");
        init2Method.addStatement("android.widget.Toast.makeText" +
                "(activity, $S,android.widget.Toast.LENGTH_SHORT).show()", "bindMethod build");

        MethodSpec.Builder unBindMethod = MethodSpec.methodBuilder(Containers.METHOD_UNBIND)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.OBJECT, "object");
        unBindMethod.addStatement("android.widget.Toast.makeText" +
                "(((MainActivity)object), $S,android.widget.Toast.LENGTH_SHORT).show()", "unBindMethod build");
        //generaClass
        TypeSpec injectClass = TypeSpec.classBuilder(typeElement.getSimpleName() + Containers.INJECTOR)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Containers.UNBIND)
                .addMethod(initMethod.build())
                .addMethod(init2Method.build())
                .addMethod(unBindMethod.build())
                .build();
        String packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();

//        for (BindViewField field : mFields) {
//            // find views
//            bindViewMethod.addStatement("host.$N = ($T)(finder.findView(source, $L))", field.getFieldName(), ClassName.get(field.getFieldType()), field.getResId());
//        }

//        MethodSpec.Builder unBindViewMethod = MethodSpec.methodBuilder("unBindView")
//                .addModifiers(Modifier.PUBLIC)
//                .addParameter(TypeName.get(mTypeElement.asType()), "host")
//                .addAnnotation(Override.class);
//        for (BindViewField field : mFields) {
//            unBindViewMethod.addStatement("host.$N = null", field.getFieldName());
//        }


        return JavaFile.builder(packageName, injectClass).build();
    }
}
