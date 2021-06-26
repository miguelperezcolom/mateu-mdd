package io.mateu.core;

import java.io.PrintWriter;
import java.io.StringWriter;

import io.mateu.components.FormComponent;
import io.mateu.model.Method;
import io.mateu.model.Parameter;
import io.mateu.model.ParsedClass;

public class BuilderImpleWriter {
    public void write(PrintWriter file, ParsedClass type) {
        //type.getMethods().forEach(this::createComponentClass);

        String className = type.getClassName();
        String simpleClassName = type.getSimpleClassName();

        System.out.println("MateuBuilderAnnotationProcessor running on " + simpleClassName);

        String generatedFullClassName = className + "Impl";
        String pkgName = generatedFullClassName.substring(0, generatedFullClassName.lastIndexOf("."));
        String generatedClassName = generatedFullClassName.substring(generatedFullClassName.lastIndexOf(".") + 1);


        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);

        out.println("package " + pkgName + ";");
        out.println("import " + className + ";");

        out.println("");

        out.println("");


        out.println();

        out.println("public class " + generatedClassName + " implements " + className + " {");

        type.getMethods().forEach(m -> {
            writeMethod(out, m);
        });

        out.println("");
        out.println("}");

        System.out.println(sw.toString());
        file.println(sw.toString());
        file.close();


    }

    private void writeMethod(PrintWriter out, Method m) {

        String generics = "";
        for (ParsedClass typeArgument : m.getReturnType().getTypeArguments()) {
            if (!"".equals(generics)) generics += ", ";
            generics += typeArgument.getClassName();
        }
        if (!"".equals(generics)) generics = "<" + generics + ">";

        String params = "";
        for (Parameter parameter : m.getParameters()) {
            if (!"".equals(params)) params += ", ";
            params += parameter.getType().getClassName() + " " + parameter.getName();
        }

        out.println("  public " + m.getReturnType().getClassName() + generics + " " + m.getName() + "(" + params + ") {");
        if (FormComponent.class.getName().equals(m.getReturnType().getCleanClassName())) {
            out.println("   return new " + m.getReturnType().getTypeArguments().get(0).getClassName() + "FormImpl" + "();");
        } else {
            out.println("   return null;");
        }
        out.println(" }");
    }
}
