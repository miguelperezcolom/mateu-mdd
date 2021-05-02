package io.mateu.mdd;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;

public class Mateu {

    public static Component createCrudComponent(Class entityClass) {
        return new Div(new Label("CRUD"));
    }

    public static Component createFormComponent(Object model) {
        return new Div(new Label("FORM"));
    }

}
