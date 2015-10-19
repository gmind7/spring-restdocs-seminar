package io.example.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by gmind on 2015-10-05.
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class PatientInput implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonUnwrapped
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

}
