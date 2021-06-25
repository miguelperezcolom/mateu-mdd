package io.mateu.model;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class Field extends Annotated {
    private final String type;
    private final String name;

    public Field(String type, String name, List<Annotation> annotations) {
        super(annotations);
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
