package com.example.demo.domain;

import com.example.demo.domain.prototypes.PersonaForm;
import com.example.demo.domain.views.CrudView;
import com.example.demo.domain.views.CrudViewCustomized;
import com.example.demo.domain.views.CrudViewFromJpql;
import com.example.demo.domain.views.CrudViewFromJpqlAnnotation;
import com.example.demo.domain.views.EditorView;
import com.example.demo.domain.views.FormView;
import com.example.demo.domain.views.FormViewWithSupplierAndConsumer;
import org.vaadin.example.addon.TheAddon;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

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
