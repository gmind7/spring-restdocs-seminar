package io.example.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.example.common.AbstractPersistable;
import io.example.common.LocalDateToDateConverter;
import io.example.common.LocalTimeToTimeConverter;
import io.example.doctor.Doctor;
import io.example.patient.Patient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by gmind on 2015-10-05.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"doctor","patient"})
@ToString(callSuper = true, exclude = {"doctor","patient"})
@Entity
public class Schedule extends AbstractPersistable<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    public Schedule(Long id) {
        this.setId(id);
    }

    @Column(nullable = false)
    @Convert(converter = LocalDateToDateConverter.class)
    private LocalDate appointmentDay;

    @Column(nullable = false)
    @Convert(converter = LocalTimeToTimeConverter.class)
    private LocalTime startTime;

    @Column(nullable = false)
    @Convert(converter = LocalTimeToTimeConverter.class)
    private LocalTime endTime;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Doctor doctor;

    @JsonIgnore
    @ManyToOne
    private Patient patient;

}
