package com.example.demo.domain.views;

import com.example.demo.domain.MyBuilder;
import io.mateu.Mateu;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("jpql")
public class CrudViewFromJpql extends Div {

    public CrudViewFromJpql() {
        add(Mateu.getInstance(MyBuilder.class).getCrudForPersonFromJpql("select p.name from Person"));
    }

}
