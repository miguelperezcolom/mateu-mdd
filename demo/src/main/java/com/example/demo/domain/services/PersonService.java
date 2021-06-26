package com.example.demo.domain.services;

import java.util.List;

import com.example.demo.domain.model.PersonForm;
import com.example.demo.domain.model.PersonRow;
import com.example.demo.domain.model.PersonSearchForm;

//todo: generar automáticamente
//todo: registrar por service locator
public class PersonService {

    public PersonForm save(PersonForm p) {
        //todo: a generar

        // buscar servicio custom
        // por service locator
        // en caso springboot, utilizar anotación para generar un service locator especial

        // si existe, utilizarlo
        // si no existe, seguir
        return p;
    }

    public PersonForm load(String k) {
        return new PersonForm();
    }

    public int count(PersonSearchForm f) {
        return 0;
    }

    public List<PersonRow> search(PersonSearchForm f, int offset, int limit) {
        return null;
    }


}
