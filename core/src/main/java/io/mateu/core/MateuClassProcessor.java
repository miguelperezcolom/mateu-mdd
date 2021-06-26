package io.mateu.core;

import java.io.IOException;

import io.mateu.annotations.MateuBuilder;
import io.mateu.components.CrudComponent;
import io.mateu.components.FormComponent;
import io.mateu.model.Method;
import io.mateu.model.ParsedClass;

public class MateuClassProcessor {

    private final PrintWriterProvider writerProvider;

    public MateuClassProcessor(PrintWriterProvider writerProvider) {
        this.writerProvider = writerProvider;
    }

    public void process(ParsedClass type) {
        if (type.getAnnotation(MateuBuilder.class) != null) {
            type.getMethods().forEach(this::createComponentClass);
            writeBuilderImpl(type);
        }
    }

    private void writeBuilderImpl(ParsedClass type) {
        try {
            new BuilderImpleWriter().write(writerProvider.create(type.getClassName() + "Impl"), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createComponentClass(Method method) {
        String className = method.getReturnType().getCleanClassName();
        try {
            if (CrudComponent.class.getName().equalsIgnoreCase(className)) {
                createCrudClass(method.getReturnType());
            } else if (FormComponent.class.getName().equalsIgnoreCase(className)) {
                createFormClass(method.getReturnType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createFormClass(ParsedClass type) throws IOException {
        return new FormClassCreator().createFormClass(writerProvider.create(type.getTypeArguments().get(0).getClassName() + "FormImpl"), type);
    }

    private String createCrudClass(ParsedClass type) throws IOException {
        return new FormClassCreator().createFormClass(writerProvider.create(type.getTypeArguments().get(0).getClassName() + "FormImpl"), type);
    }
}
