package io.example.doctor;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by gmind on 2015-10-05.
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class DoctorInput implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonUnwrapped
    private Long id;

    @NonNull
    private String name;

}
