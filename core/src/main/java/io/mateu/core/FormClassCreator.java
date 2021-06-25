package io.mateu.core;

import java.io.PrintWriter;

import io.mateu.annotations.Action;
import io.mateu.fieldProcessors.FieldProcessors;
import io.mateu.model.ParsedClass;

public class FormClassCreator {

    public String createFormClass(PrintWriter out, ParsedClass type) {
        ParsedClass modelType = type.getTypeArguments().get(0);
        String modelClassName = modelType.getClassName();
        String modelSimpleClassName = modelType.getSimpleClassName();

        String className = modelType.getClassName();
        String simpleClassName = modelType.getSimpleClassName();

        System.out.println("MateuBuilderAnnotationProcessor running on " + simpleClassName);

        String generatedFullClassName = className + "FormImpl";
        String pkgName = generatedFullClassName.substring(0, generatedFullClassName.lastIndexOf("."));
        String generatedClassName = generatedFullClassName.substring(generatedFullClassName.lastIndexOf(".") + 1);




        out.println("package " + pkgName + ";");
        out.println("import " + modelClassName + ";");

        out.println("");

        out.println("import com.vaadin.flow.component.Component;\n" +
                "import com.vaadin.flow.component.button.Button;\n" +
                "import com.vaadin.flow.component.checkbox.Checkbox;\n" +
                "import com.vaadin.flow.component.datepicker.DatePicker;\n" +
                "import com.vaadin.flow.component.notification.Notification;\n" +
                "import com.vaadin.flow.component.orderedlayout.HorizontalLayout;\n" +
                "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" +
                "import com.vaadin.flow.component.textfield.NumberField;\n" +
                "import com.vaadin.flow.component.textfield.IntegerField;\n" +
                "import com.vaadin.flow.component.textfield.TextField;\n" +
                "import com.vaadin.flow.data.binder.Binder;\n" +
                "import com.vaadin.flow.data.binder.ValidationException;\n" +
                "import com.vaadin.flow.router.Route;\n");


        out.println();

        out.println("public class " + generatedClassName + " extends io.mateu.mdd.components.FormComponent<" + modelSimpleClassName + "> {");

        out.println("    Binder binder = new Binder(" + modelSimpleClassName + ".class);\n" +
                "\n" +
                "    public " + generatedClassName + "() {\n" +
                "\n" +
                "        " + modelSimpleClassName + " initialData = new " + modelSimpleClassName + "();\n" +
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
        modelType.getFields().forEach(f -> {
            out.println(FieldProcessors.process(f));
        });
        out.println("    }");

        out.println("    private void addActionsBar() {");
        out.println("\n" +
                "        HorizontalLayout actionsBar = new HorizontalLayout();\n");

        modelType.getMethods().forEach(m -> {
            if (m.getAnnotation(Action.class) != null && m.isPublic()) {
                out.println(                        "\n" +
                        "        actionsBar.add(new Button(\"" + m.getName() + "\", e -> {\n" +
                        "            if (binder.validate().isOk()) {\n" +
                        "                " + modelSimpleClassName + " p = new " + modelSimpleClassName + "();\n" +
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
        });

        out.println("\n" +
                "        add(actionsBar);\n" +
                "    }\n");


        out.println("");
        out.println("}");




        return generatedFullClassName;
    }

}
