package io.mateu;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.google.common.collect.Lists;
import io.mateu.components.FormComponent;
import io.mateu.core.FormClassCreator;
import io.mateu.model.Field;
import io.mateu.model.ParsedClass;
import org.junit.Test;

public class FormClassCreatorTest
{

    @Test
    public void shouldAnswerWithTrue()
    {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        new FormClassCreator().createFormClass(pw, createFormParsedClass());
        System.out.println(sw.toString());
    }

    private ParsedClass createFormParsedClass() {
        ParsedClass type = new ParsedClass(
                Lists.newArrayList(),
                FormComponent.class.getName(),
                Lists.newArrayList(),
                Lists.newArrayList(),
                Lists.newArrayList(createParsedClass())
        );
        return type;
    }

    private ParsedClass createParsedClass() {
        ParsedClass type = new ParsedClass(
                Lists.newArrayList(),
                "org.example.MiFormulario",
                Lists.newArrayList(new Field(String.class.toString(), "name", Lists.newArrayList())),
                Lists.newArrayList(),
                Lists.newArrayList()
        );
        return type;
    }
}
