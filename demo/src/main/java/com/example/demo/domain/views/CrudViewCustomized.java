package com.example.demo.domain.views;

import com.example.demo.domain.MyBuilder;
import io.mateu.Mateu;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("customcrud")
public class CrudViewCustomized extends Div {

    public CrudViewCustomized() {
        add(Mateu.getInstance(MyBuilder.class).getCustomizedCrudForPerson());
    }

}
