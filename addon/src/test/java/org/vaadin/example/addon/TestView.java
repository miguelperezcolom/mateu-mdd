package org.vaadin.example.addon;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.addon.prototypes.PersonaForm;
import org.vaadin.example.addon.views.CrudView;
import org.vaadin.example.addon.views.FormView;

@Route("")
public class TestView extends VerticalLayout {

    public TestView() {
        TheAddon theAddon = new TheAddon();
        theAddon.setId("theAddon");
        add(theAddon);
        add(new RouterLink("Crud", CrudView.class));
        add(new RouterLink("Form", FormView.class));
        add(new RouterLink( "Prototype for form of Persona", PersonaForm.class));
    }
}
