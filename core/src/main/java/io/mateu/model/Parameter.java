package io.mateu.model;

import java.lang.annotation.Annotation;
import java.util.List;

public class Parameter extends Field {
    public Parameter(ParsedClass type, String name, List<Annotation> annotations) {
        super(type, name, annotations);
    }
}
