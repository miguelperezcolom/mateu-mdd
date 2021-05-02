package org.vaadin.example.addon.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import io.mateu.mdd.Mateu;
import org.vaadin.example.addon.MyBuilder;
import org.vaadin.example.addon.model.Persona;

@Route("crud")
public class CrudView extends Div {

    public CrudView() {
        add(Mateu.getInstance(MyBuilder.class).getCrudForPerson());
    }

}
