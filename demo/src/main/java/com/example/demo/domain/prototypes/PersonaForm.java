package com.example.demo.domain.prototypes;

import com.example.demo.domain.model.Person;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;


@Route("protoformpersona")
public class PersonaForm extends VerticalLayout {

    Binder binder = new Binder(Person.class);

    public PersonaForm() {

        Person initialData = new Person();
        initialData.setName("Mateu");
        
        addClassName("form");


        addFields();


        binder.readBean(initialData);


        addActionsBar();

    }

    private void addFields() {
        {
            TextField field;
            add(field = new TextField("Name"));
            binder.forField(field).bind("name");
        }

        {
            IntegerField field;
            add(field = new IntegerField("Age"));
            binder.forField(field).bind("age");
        }

        {
            DatePicker field;
            add(field = new DatePicker("Birth date"));
            binder.forField(field).bind("birthDate");
        }

        {
            Checkbox field;
            add(field = new Checkbox("Registered"));
            binder.forField(field).bind("registered");
        }
    }

    private void addActionsBar() {

        HorizontalLayout actionsBar = new HorizontalLayout();

        actionsBar.add(new Button("Save", e -> {
            if (binder.validate().isOk()) {
                Person p = new Person();
                try {
                    binder.writeBean(p);
                } catch (ValidationException validationException) {
                    validationException.printStackTrace();
                }
                Notification.show("saved " + p.getName());
            }
        }));

        actionsBar.add(new Button("Send", e -> {
            if (binder.validate().isOk()) {
                Person p = new Person();
                try {
                    binder.writeBean(p);
                } catch (ValidationException validationException) {
                    validationException.printStackTrace();
                }
                Notification.show("sent " + p.getName());
            }
        }));

        add(actionsBar);
    }

}
