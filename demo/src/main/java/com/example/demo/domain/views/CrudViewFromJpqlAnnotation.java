package com.example.demo.domain.views;

import com.example.demo.domain.MyBuilder;
import io.mateu.Mateu;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("jpqlannotation")
public class CrudViewFromJpqlAnnotation extends Div {

    public CrudViewFromJpqlAnnotation() {
        add(Mateu.getInstance(MyBuilder.class).getCrudForPersonFromJpqlAnnotation());
    }

}
