package org.vaadin.example.addon.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import io.mateu.Mateu;
import org.vaadin.example.addon.MyBuilder;

@Route("jpqlannotation")
public class CrudViewFromJpqlAnnotation extends Div {

    public CrudViewFromJpqlAnnotation() {
        add(Mateu.getInstance(MyBuilder.class).getCrudForPersonFromJpqlAnnotation());
    }

}
