package io.mateu.annotationProcessor.parser;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import com.google.common.collect.Lists;
import io.mateu.model.Field;
import io.mateu.model.ParsedClass;

public class ElementParser {

    public ParsedClass parse(RoundEnvironment roundEnv, TypeElement typeElement) {
        //todo: implementar
        return new ParsedClass(Lists.newArrayList(),
                "org.example.MiFormulario",
                Lists.newArrayList(),
                Lists.newArrayList(),
                Lists.newArrayList());
    }

}
