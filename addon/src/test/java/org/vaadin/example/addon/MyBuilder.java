package org.vaadin.example.addon;

import io.mateu.mdd.annotations.MateuBuilder;
import io.mateu.mdd.components.CrudComponent;
import io.mateu.mdd.components.FormComponent;
import org.vaadin.example.addon.model.Person;

/**
 * To be injected or created using Mateu.getInstance()
 */
@MateuBuilder
public interface MyBuilder {

    CrudComponent<Person> getCrudForPerson();

    FormComponent<Person> getFormForPerson();

}
