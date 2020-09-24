package io.mateu.showcase.app;

import io.mateu.mdd.core.annotations.Action;
import org.example.domain.boundaries.common.entities.Person;
import org.example.domain.boundaries.educational.entities.AcademicPlan;
import org.example.domain.boundaries.educational.entities.Classroom;
import org.example.domain.boundaries.educational.entities.Course;

public class ConfigMenu {

    @Action
    Class plans = AcademicPlan.class;

    @Action
    Class courses = Course.class;

    @Action
    Class persons = Person.class;

    @Action
    Class classrooms = Classroom.class;



}
