package org.vaadin.example.addon.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import io.mateu.mdd.Mateu;
import org.vaadin.example.addon.MyBuilder;

@Route("form")
public class FormView extends Div {

    public FormView() {
        add(Mateu.getInstance(MyBuilder.class).getFormForPerson());
    }
}
