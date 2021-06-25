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

    private ParsedClass createFormParsedClass() {
        ParsedClass type = new ParsedClass(
                new HashMap<>(),
                Lists.newArrayList(),
                FormComponent.class.getName(),
                Lists.newArrayList(),
                Lists.newArrayList(),
                () -> Lists.newArrayList(createParsedClass())
        );
        return type;
    }

    private ParsedClass createParsedClass() {
        ParsedClass type = new ParsedClass(
                new HashMap<>(),
                Lists.newArrayList(),
                "org.example.MiFormulario",
                Lists.newArrayList(
                        new Field(new ParsedClass(new HashMap<>(), String.class.getName()), "name", Lists.newArrayList())
                        , new Field(new ParsedClass(new HashMap<>(), int.class.getName()), "age", Lists.newArrayList())
                        , new Field(new ParsedClass(new HashMap<>(), double.class.getName()), "rating", Lists.newArrayList())
                        , new Field(new ParsedClass(new HashMap<>(), boolean.class.getName()), "subscribed", Lists.newArrayList())
                ),
                Lists.newArrayList(
                        new Method(
                                new ParsedClass(new HashMap<>(), void.class.getName())
                                , "doSomething"
                                , Lists.newArrayList(createActionAnnotation())
                                , AccessLevel.Public
                        )
                ),
                () -> Lists.newArrayList()
        );
        return type;
    }

    private Action createActionAnnotation() {
        Map<String, Object> annotationParameters = new HashMap<>();
        //annotationParameters.put("name", "someName");
        Action myAnnotation = null;
        try {
            myAnnotation = TypeFactory.annotation(Action.class, annotationParameters);
        } catch (AnnotationFormatException e) {
            e.printStackTrace();
        }
        return myAnnotation;
    }
}
