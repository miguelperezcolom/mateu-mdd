package io.mateu.mdd.core.model.config;

import lombok.Getter;
import lombok.MateuMDDEntity;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by miguel on 18/4/17.
 */
@Entity
@Getter
@Setter@MateuMDDEntity
public class DummyDate {

    @Id
    private LocalDate value;

    private LocalDateTime pickupTimeInformed;

    public DummyDate() {

    }

    public DummyDate(LocalDate date) {
        value = date;
    }
}
