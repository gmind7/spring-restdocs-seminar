package io.example.schedule;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by gmind on 2015-10-05.
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class ScheduleInput implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonUnwrapped
    private Long id;

    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate appointmentDay;

    @NonNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NonNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @NonNull
    private Long doctorId;

    private Long patientId;

}
