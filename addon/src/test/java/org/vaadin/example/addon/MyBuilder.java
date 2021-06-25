package org.vaadin.example.addon;

import io.mateu.annotations.MateuBuilder;
import io.mateu.components.CrudComponent;
import io.mateu.components.FormComponent;
import org.vaadin.example.addon.model.Person;

/**
 * To be injected or created using Mateu.getInstance()
 */
@MateuBuilder
public interface MyBuilder {

    CrudComponent<Person> getCrudForPerson();

    FormComponent<Person> getFormForPerson();

}
