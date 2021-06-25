package io.mateu.model;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public abstract class Annotated {

    private List<Annotation> annotations = new ArrayList<>();

    public Annotated(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public <T> T getAnnotation(Class<T> actionClass) {
        T a = null;
        for (Annotation annotation : annotations) {
            if (actionClass.isAssignableFrom(annotation.getClass())) {
                a = (T) annotation;
                break;
            }
        }
        return a;
    }

}
