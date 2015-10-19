package io.example.doctor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.example.schedule.Schedule;
import io.example.common.AbstractPersistable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

/**
 * Created by gmind on 2015-10-05.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "schedules")
@ToString(callSuper = true, exclude = "schedules")
@Entity
public class Doctor extends AbstractPersistable<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    public Doctor(Long id) {
        this.setId(id);
    }

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE)
    private List<Schedule> schedules;

}
