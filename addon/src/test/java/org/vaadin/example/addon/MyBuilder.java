package org.vaadin.example.addon;

import io.mateu.annotations.Jpql;
import io.mateu.annotations.MateuBuilder;
import io.mateu.components.CrudComponent;
import io.mateu.components.CustomizedCrudComponent;
import io.mateu.components.FormComponent;
import org.vaadin.example.addon.model.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * To be injected or created using Mateu.getInstance()
 */
@MateuBuilder
public interface MyBuilder {

    CrudComponent<Person> getCrudForPerson();

    FormComponent<Person> getFormForPerson();

    FormComponent<Person> getEditorForPerson(Supplier<Person> loader, Consumer<Person> save);

    FormComponent<Person> getFormForPerson(Consumer<Person> save, Consumer<Person> process);

    @Jpql("select p.name from Person p")
    CrudComponent<Person> getCrudForPersonFromJpqlAnnotation();

    CrudComponent<Person> getCrudForPersonFromJpql(String jpql);

    CustomizedCrudComponent<PersonSearchForm, PersonRow, PersonForm, PersonService> getCustomizedCrudForPerson();

}
