package org.vaadin.example.addon.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import io.mateu.Mateu;
import org.vaadin.example.addon.MyBuilder;

@Route("jpql")
public class CrudViewFromJpql extends Div {

    public CrudViewFromJpql() {
        add(Mateu.getInstance(MyBuilder.class).getCrudForPersonFromJpql("select p.name from Person"));
    }

}
