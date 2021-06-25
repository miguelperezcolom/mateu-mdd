package org.vaadin.example.addon;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.addon.prototypes.PersonaForm;
import org.vaadin.example.addon.views.*;

@Route("")
public class TestView extends VerticalLayout {

    public TestView() {
        TheAddon theAddon = new TheAddon();
        theAddon.setId("theAddon");
        add(theAddon);
        add(new RouterLink("Crud", CrudView.class));
        add(new RouterLink("Form", FormView.class));
        add(new RouterLink( "Prototype for form of Persona", PersonaForm.class));
        add(new RouterLink( "Crud customized", CrudViewCustomized.class));
        add(new RouterLink( "Crud from jpql", CrudViewFromJpql.class));
        add(new RouterLink( "Crud from jpql annotation", CrudViewFromJpqlAnnotation.class));
        add(new RouterLink( "Editor", EditorView.class));
        add(new RouterLink( "Form with supplier and consumer", FormViewWithSupplierAndConsumer.class));
    }
}
