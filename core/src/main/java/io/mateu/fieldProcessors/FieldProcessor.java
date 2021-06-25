package io.mateu.fieldProcessors;

import io.mateu.model.Field;


public interface FieldProcessor {

    int getOrder();

    boolean matches(Field field);

    String writeCode(Field field);

}
