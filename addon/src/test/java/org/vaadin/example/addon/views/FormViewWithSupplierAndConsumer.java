package org.vaadin.example.addon.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import io.mateu.Mateu;
import org.vaadin.example.addon.MyBuilder;

@Route("formsupplierconsumer")
public class FormViewWithSupplierAndConsumer extends Div {

    public FormViewWithSupplierAndConsumer() {
        add(Mateu.getInstance(MyBuilder.class).getFormForPerson(p -> System.out.println("saved"), p -> System.out.println("processed")));
    }
}
