package io.mateu.mdd.annotationProcessor;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import io.mateu.mdd.annotations.Action;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;

public class FormClassCreator {

    public String createFormClass(ProcessingEnvironment processingEnv, Type type) {
        Type.ClassType classType = (Type.ClassType) type;
        Type.ClassType modelType = (Type.ClassType) classType.getTypeArguments().get(0);
        String modelClassName = modelType.toString();
        Symbol.TypeSymbol modelElement = modelType.asElement();// Person

        TypeElement typeElement = (TypeElement) type.asElement();
        String className = typeElement.getQualifiedName().toString();
        String simpleClassName = typeElement.getSimpleName().toString();

        System.out.println("MateuBuilderAnnotationProcessor running on " + simpleClassName);

        String generatedFullClassName = className + "FormImpl";
        String pkgName = generatedFullClassName.substring(0, generatedFullClassName.lastIndexOf("."));
        String generatedClassName = generatedFullClassName.substring(generatedFullClassName.lastIndexOf(".") + 1);


        JavaFileObject builderFile = null;
        try {
            builderFile = processingEnv.getFiler().createSourceFile(generatedFullClassName);
            try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                // writing generated file to out …

                out.println("package " + pkgName + ";");
                out.println("import " + className + ";");
                out.println("import " + modelClassName + ";");

                out.println("");

                out.println("import com.vaadin.flow.component.Component;\n" +
                        "import com.vaadin.flow.component.button.Button;\n" +
                        "import com.vaadin.flow.component.checkbox.Checkbox;\n" +
                        "import com.vaadin.flow.component.datepicker.DatePicker;\n" +
                        "import com.vaadin.flow.component.notification.Notification;\n" +
                        "import com.vaadin.flow.component.orderedlayout.HorizontalLayout;\n" +
                        "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" +
                        "import com.vaadin.flow.component.textfield.IntegerField;\n" +
                        "import com.vaadin.flow.component.textfield.TextField;\n" +
                        "import com.vaadin.flow.data.binder.Binder;\n" +
                        "import com.vaadin.flow.data.binder.ValidationException;\n" +
                        "import com.vaadin.flow.router.Route;\n");


                out.println();

                out.println("public class " + generatedClassName + " extends io.mateu.mdd.components.FormComponent<Person> {");

                out.println("    Binder binder = new Binder(Person.class);\n" +
                        "\n" +
                        "    public " + generatedClassName + "() {\n" +
                        "\n" +
                        "        " + modelClassName + " initialData = new " + modelClassName + "();\n" +
                        "        initialData.setName(\"Mateu\");\n" +
                        "\n" +
                        "        addClassName(\"form\");\n" +
                        "\n" +
                        "\n" +
                        "        addFields();\n" +
                        "\n" +
                        "\n" +
                        "        binder.readBean(initialData);\n" +
                        "\n" +
                        "\n" +
                        "        addActionsBar();\n" +
                        "\n" +
                        "    }\n");

                out.println("    private void addFields() {");
                for (Element enclosedElement : ElementFilter.fieldsIn(modelElement.getEnclosedElements())) {
                    System.out.println(enclosedElement.getClass().getName());
                    if (enclosedElement instanceof Symbol.MethodSymbol) {
                    } else {
                        out.println(                        "        {\n" +
                                "            TextField field;\n" +
                                "            add(field = new TextField(\"Name\"));\n" +
                                "            binder.forField(field).bind(\"name\");\n" +
                                "        }\n"
                        );
                    }
                }
                out.println("    }");

                out.println("    private void addActionsBar() {");
                out.println("\n" +
                        "        HorizontalLayout actionsBar = new HorizontalLayout();\n");

                for (Element enclosedElement : modelElement.getEnclosedElements()) {
                    System.out.println(enclosedElement.getClass().getName());
                    if (enclosedElement instanceof Symbol.MethodSymbol) {
                        Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol) enclosedElement;

                        if (methodSymbol.getAnnotation(Action.class) != null && ((methodSymbol.flags() & Flags.PUBLIC) == 1)) {

                            /*
                            out.println("  public " + methodSymbol.getReturnType().toString() + " " + methodSymbol.getSimpleName() + "() {");
                            out.println("    return null;");
                            out.println("  }");

                             */

                            out.println(                        "\n" +
                                    "        actionsBar.add(new Button(\"" + methodSymbol.getSimpleName() + "\", e -> {\n" +
                                    "            if (binder.validate().isOk()) {\n" +
                                    "                Person p = new Person();\n" +
                                    "                try {\n" +
                                    "                    binder.writeBean(p);\n" +
                                    "                } catch (ValidationException validationException) {\n" +
                                    "                    validationException.printStackTrace();\n" +
                                    "                }\n" +
                                    "                Notification.show(\"saved \" + p.getName());\n" +
                                    "            }\n" +
                                    "        }));\n"
                            );

                        }

                    }
                }

                        out.println("\n" +
                        "        add(actionsBar);\n" +
                        "    }\n");


                out.println("");
                out.println("}");
            }



        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return generatedFullClassName;
    }

}
