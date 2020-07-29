package com.plumcookingwine.irouter.compiler;

import com.google.auto.service.AutoService;
import com.plumcookingwine.irouter.annotation.Parameter;
import com.plumcookingwine.irouter.annotation.bean.RouterBean;
import com.plumcookingwine.irouter.compiler.factory.ParameterFactory;
import com.plumcookingwine.irouter.compiler.utils.ProcessorConfig;
import com.plumcookingwine.irouter.compiler.utils.ProcessorUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({ProcessorConfig.PARAMETER_PACKAGE})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ParameterProcessor extends AbstractProcessor {

    private Elements elementUtils; // 类信息
    private Types typeUtils; // 具体类型
    private Messager messager; // 日志
    private Filer filer; // 生成文件

    // 临时map存储，用来存放被@Parameter注解的属性集合，生成类文件时遍历
    // key:类节点, value:被@Parameter注解的属性集合
    private Map<TypeElement, List<Element>> tempParameterMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        if (!ProcessorUtils.isEmpty(set)) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Parameter.class);

            if (!ProcessorUtils.isEmpty(elements)) {

                for (Element element : elements) {
                    TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

                    if (tempParameterMap.containsKey(enclosingElement)) {
                        tempParameterMap.get(enclosingElement).add(element);
                    } else {
                        List<Element> fields = new ArrayList<>();
                        fields.add(element);
                        tempParameterMap.put(enclosingElement, fields);
                    }
                } // end for

                // 生成类文件
                // 判断是否有需要生成的类文件
                if (ProcessorUtils.isEmpty(tempParameterMap)) return true;
                TypeElement activityType = elementUtils.getTypeElement(ProcessorConfig.ACTIVITY_PACKAGE);
                TypeElement parameterType = elementUtils.getTypeElement(ProcessorConfig.IROUTER_API_PARAMETER_GET);
                TypeElement fragmentType = elementUtils.getTypeElement(ProcessorConfig.FRAGMENT_PACKAGE);

                ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.OBJECT, ProcessorConfig.PARAMETER_NAME).build();

                for (Map.Entry<TypeElement, List<Element>> entry : tempParameterMap.entrySet()) {

                    TypeElement typeElement = entry.getKey();
                    if (!typeUtils.isSubtype(typeElement.asType(), activityType.asType()) &&
                            !typeUtils.isSubtype(typeElement.asType(), fragmentType.asType())) {
                        throw new RuntimeException("@Parameter注解目前仅限用于Activity和Fragment类之上");
                    }
                    ClassName className = ClassName.get(typeElement);
                    ParameterFactory factory = new ParameterFactory.Builder(parameterSpec)
                            .setMessager(messager)
                            .setClassName(className)
                            .build();
                    factory.addFirstStatement();
                    // 添加bundle
                    TypeElement bundleType = elementUtils.getTypeElement("android.os.Bundle");
                    ClassName bundleClassName = ClassName.get(bundleType);
                    if (typeUtils.isSubtype(typeElement.asType(), activityType.asType())) {
                        factory.addBundleStatement(RouterBean.TypeEnum.ACTIVITY, bundleClassName);
                    } else if (typeUtils.isSubtype(typeElement.asType(), fragmentType.asType())) {
                        factory.addBundleStatement(RouterBean.TypeEnum.FRAGMENT, bundleClassName);
                    }

                    for (Element fieldElement : entry.getValue()) {
                        factory.buildStatement(fieldElement);
                    }

                    String finalClassName = typeElement.getSimpleName() + ProcessorConfig.PARAMETER_FILE_NAME;
                    messager.printMessage(Diagnostic.Kind.NOTE, "APT gen get params：" +
                            className.packageName() + "." + finalClassName);

                    try {
                        JavaFile.builder(className.packageName(), // 包名
                                TypeSpec.classBuilder(finalClassName) // 类名
                                        .addSuperinterface(ClassName.get(parameterType)) // 实现ParameterLoad接口
                                        .addModifiers(Modifier.PUBLIC) // public修饰符
                                        .addMethod(factory.build()) // 方法的构建（方法参数 + 方法体）
                                        .build()) // 类构建完成
                                .build() // JavaFile构建完成
                                .writeTo(filer); // 文件生成器开始生成类文件
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
