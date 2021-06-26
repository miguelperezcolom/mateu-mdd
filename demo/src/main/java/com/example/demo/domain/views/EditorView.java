package com.example.demo.domain.views;

import com.example.demo.domain.MyBuilder;
import com.example.demo.domain.model.Person;
import io.mateu.Mateu;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("editor")
public class EditorView extends Div {

    public EditorView() {
        add(Mateu.getInstance(MyBuilder.class).getEditorForPerson(() -> new Person(), p -> System.out.println("saved")));
    }
}
