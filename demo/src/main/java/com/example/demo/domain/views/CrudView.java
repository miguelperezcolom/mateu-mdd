package com.example.demo.domain.views;

import com.example.demo.domain.MyBuilder;
import io.mateu.Mateu;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("crud")
public class CrudView extends Div {

    public CrudView() {
        add(Mateu.getInstance(MyBuilder.class).getCrudForPerson());
    }

}
