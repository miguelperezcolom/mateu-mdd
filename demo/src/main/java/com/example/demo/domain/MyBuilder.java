package com.example.demo.domain;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.example.demo.domain.model.Person;
import com.example.demo.domain.model.PersonForm;
import com.example.demo.domain.model.PersonRow;
import com.example.demo.domain.model.PersonSearchForm;
import com.example.demo.domain.model.PersonService;
import io.mateu.annotations.Jpql;
import io.mateu.annotations.MateuBuilder;
import io.mateu.components.CrudComponent;
import io.mateu.components.CustomizedCrudComponent;
import io.mateu.components.FormComponent;

/**
 * To be injected or created using Mateu.getInstance()
 */
@MateuBuilder
public interface MyBuilder {

    CrudComponent<Person> getCrudForPerson();

    FormComponent<Person> getFormForPersonWithConsumers();

    FormComponent<Person> getEditorForPerson(Supplier<Person> loader, Consumer<Person> save);

    FormComponent<Person> getFormForPersonWithConsumers(Consumer<Person> save, Consumer<Person> process);

    @Jpql("select p.name from Person p")
    CrudComponent<Person> getCrudForPersonFromJpqlAnnotation();

    CrudComponent<Person> getCrudForPersonFromJpql(String jpql);

    CustomizedCrudComponent<PersonSearchForm, PersonRow, PersonForm, PersonService> getCustomizedCrudForPerson();

}
