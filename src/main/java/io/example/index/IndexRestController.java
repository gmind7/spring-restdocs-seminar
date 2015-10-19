package io.example.index;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import io.example.doctor.DoctorRestController;
import io.example.patient.PatientRestController;
import io.example.schedule.ScheduleRestController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gmind on 2015-10-06.
 */
@RestController
@RequestMapping(value = "/")
public class IndexRestController {

    @RequestMapping(method= RequestMethod.GET)
    public ResourceSupport index() {
        ResourceSupport index = new ResourceSupport();
        index.add(linkTo(DoctorRestController.class).withRel("doctors"));
        index.add(linkTo(PatientRestController.class).withRel("patient"));
        index.add(linkTo(ScheduleRestController.class).withRel("schedule"));
        return index;
    }

}
