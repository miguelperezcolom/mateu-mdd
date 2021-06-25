package io.mateu.model;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

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
        this.packageName = className.substring(0, className.lastIndexOf('.'));
        this.className = className;
        this.simpleClassName = className.substring(className.lastIndexOf('.') + 1);
        this.fields = fields;
        this.methods = methods;
        this.typeArguments = typeArguments;
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
}
