package io.mateu.model;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class Field extends Annotated {
    private final ParsedClass type;
    private final String name;

    public Field(ParsedClass type, String name, List<Annotation> annotations) {
        super(annotations);
        this.type = type;
        this.name = name;
    }

    public ParsedClass getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
