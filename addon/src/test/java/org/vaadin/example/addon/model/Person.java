package org.vaadin.example.addon.model;


import io.mateu.mdd.annotations.Action;

import java.time.LocalDate;

public class Person {

    private String name;

    private int age;

    private LocalDate birthDate;

    private boolean registered;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Action
    public void test() {
        System.out.println("Hola test!");
    }

}
