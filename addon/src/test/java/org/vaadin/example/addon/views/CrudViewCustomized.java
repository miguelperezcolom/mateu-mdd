package org.vaadin.example.addon.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import io.mateu.Mateu;
import org.vaadin.example.addon.MyBuilder;

@Route("customcrud")
public class CrudViewCustomized extends Div {

    public CrudViewCustomized() {
        add(Mateu.getInstance(MyBuilder.class).getCustomizedCrudForPerson());
    }

}
