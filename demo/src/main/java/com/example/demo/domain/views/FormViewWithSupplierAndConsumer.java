package com.example.demo.domain.views;

import com.example.demo.domain.MyBuilder;
import io.mateu.Mateu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("formsupplierconsumer")
public class FormViewWithSupplierAndConsumer extends Div {

    public FormViewWithSupplierAndConsumer() {
        add(Mateu.getInstance(MyBuilder.class).getFormForPerson(p -> System.out.println("saved"), p -> System.out.println("processed")));
    }
}
