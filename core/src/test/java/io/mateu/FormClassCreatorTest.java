package io.mateu;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Lists;
import io.leangen.geantyref.AnnotationFormatException;
import io.leangen.geantyref.TypeFactory;
import io.mateu.annotations.Action;
import io.mateu.components.FormComponent;
import io.mateu.core.FormClassCreator;
import io.mateu.model.AccessLevel;
import io.mateu.model.Field;
import io.mateu.model.Method;
import io.mateu.model.ParsedClass;
import org.junit.Test;

public class FormClassCreatorTest
{

    @Test
    public void shouldAnswerWithTrue() throws AnnotationFormatException {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        new FormClassCreator().createFormClass(pw, createFormParsedClass());
        System.out.println(sw.toString());
    }

    private ParsedClass createFormParsedClass() throws AnnotationFormatException {
        ParsedClass type = new ParsedClass(
                Lists.newArrayList(),
                FormComponent.class.getName(),
                Lists.newArrayList(),
                Lists.newArrayList(),
                Lists.newArrayList(createParsedClass())
        );
        return type;
    }

    private ParsedClass createParsedClass() throws AnnotationFormatException {
        ParsedClass type = new ParsedClass(
                Lists.newArrayList(),
                "org.example.MiFormulario",
                Lists.newArrayList(
                        new Field(String.class.toString(), "name", Lists.newArrayList())
                        , new Field(int.class.toString(), "age", Lists.newArrayList())
                ),
                Lists.newArrayList(
                        new Method(
                                new ParsedClass(void.class.toString())
                                , "doSomething"
                                , Lists.newArrayList(createActionAnnotation())
                                , AccessLevel.Public
                        )
                ),
                Lists.newArrayList()
        );
        return type;
    }

    private Action createActionAnnotation() throws AnnotationFormatException {
        Map<String, Object> annotationParameters = new HashMap<>();
        //annotationParameters.put("name", "someName");
        Action myAnnotation = TypeFactory.annotation(Action.class, annotationParameters);
        return myAnnotation;
    }
}
