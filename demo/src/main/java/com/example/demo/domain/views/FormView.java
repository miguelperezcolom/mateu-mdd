package com.example.demo.domain.views;

import com.example.demo.domain.MyBuilder;
import io.mateu.Mateu;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("form")
public class FormView extends Div {

    public FormView() {
        add(Mateu.getInstance(MyBuilder.class).getFormForPerson());
    }
}
