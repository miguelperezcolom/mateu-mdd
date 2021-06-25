package io.mateu.model;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class ParsedClass extends Annotated {

    private String packageName;
    private String className;
    private String simpleClassName;
    private List<Field> fields = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    //todo: Type.ClassType modelType = (Type.ClassType) classType.getTypeArguments().get(0);
    private List<ParsedClass> typeArguments = new ArrayList<>();

    public ParsedClass(List<Annotation> annotations, String className, List<Field> fields, List<Method> methods, List<ParsedClass> typeArguments) {
        super(annotations);
        this.packageName = getPackageName(className);
        this.className = className;
        this.simpleClassName = getSimpleClassName(className);
        this.fields = fields;
        this.methods = methods;
        this.typeArguments = typeArguments;
    }

    private String getSimpleClassName(String className) {
        className = cleanClassName(className);
        return className.contains(".") ? className.substring(className.lastIndexOf('.') + 1) : className;
    }

    private String getPackageName(String className) {
        className = cleanClassName(className);
        return className.contains(".") ? className.substring(0, className.lastIndexOf('.')) : "";
    }

    private String cleanClassName(String className) {
        return className.contains("<") ? className.substring(0, className.indexOf('<')) : className;
    }

    public ParsedClass(String className) {
        super(Lists.newArrayList());
        this.packageName = getPackageName(className);
        this.className = className;
        this.simpleClassName = getSimpleClassName(className);
        this.fields = Lists.newArrayList();
        this.methods = Lists.newArrayList();
        this.typeArguments = Lists.newArrayList();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Method> getMethods() {
        return methods;
    }



    public List<ParsedClass> getTypeArguments() {
        return typeArguments;
    }

    public String getCleanClassName() {
        return cleanClassName(className);
    }
}
