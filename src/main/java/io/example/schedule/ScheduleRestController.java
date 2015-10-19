package io.example.schedule;

import io.example.common.ResourceNotFoundException;
import io.example.config.mapper.AutoMapper;
import io.example.doctor.Doctor;
import io.example.doctor.DoctorRestController;
import io.example.patient.Patient;
import io.example.patient.PatientRestController;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;

import static io.example.schedule.ScheduleResourceAssembler.ScheduleResource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by gmind on 2015-10-05.
 */
@RestController
@RequestMapping(value = "/schedules")
public class ScheduleRestController {

    @Autowired
    private ScheduleJpaRepository scheduleJpaRepository;

    @Autowired
    private ScheduleResourceAssembler scheduleResourceAssembler;

    @Autowired
    private PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    private DoctorRestController doctorRestController;

    @Autowired
    private PatientRestController patientRestController;

    @Autowired
    private AutoMapper autoMapper;

    @RequestMapping(method = RequestMethod.GET)
    public PagedResources<ScheduleResource> showAll(@PageableDefault Pageable pageable) {
        Page<Schedule> schedules = this.scheduleJpaRepository.findAll(pageable);
        return this.pagedResourcesAssembler.toResource(schedules, scheduleResourceAssembler);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resource<Schedule> showOne(@PathVariable("id") Long id) {
        Schedule entity = this.scheduleJpaRepository.findOne(id);
        return this.scheduleResourceAssembler.toResource(entity);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT)
    public HttpHeaders create(@RequestBody ScheduleInput scheduleInput) {
        Schedule mapping = this.autoMapper.map(scheduleInput, Schedule.class);
        Schedule entity  = this.scheduleJpaRepository.save(mapping);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(linkTo(ScheduleRestController.class).slash(entity.getId()).toUri());
        return httpHeaders;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public void update(@PathVariable("id") Long id, @RequestBody ScheduleInput scheduleInput) {
        Schedule source  = this.autoMapper.map(scheduleInput, Schedule.class);
        Schedule target  = this.scheduleJpaRepository.findOne(id);
        Schedule mapping = this.autoMapper.map(source, target, Schedule.class);
        this.scheduleJpaRepository.save(mapping);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void  delete(@PathVariable("id") Long id) {
        this.scheduleJpaRepository.delete(id);
    }

    @RequestMapping(value = "/{id}/doctor", method = RequestMethod.GET)
    public Resource<Doctor> showDoctor(@PathVariable("id") Long id) {
        Doctor doctor = this.scheduleJpaRepository.findOne(id).getDoctor();
        if(doctor==null) {
            throw new ResourceNotFoundException("doctor");
        }
        Resource<Doctor> doctorResource = this.doctorRestController.showOne(doctor.getId());
        return doctorResource;
    }

    @RequestMapping(value = "/{id}/patient", method = RequestMethod.GET)
    public Resource<Patient> showPatient(@PathVariable("id") Long id) {
        Patient patient = this.scheduleJpaRepository.findOne(id).getPatient();
        if(patient==null) {
            throw new ResourceNotFoundException("patient");
        }
        Resource<Patient> patientResource = this.patientRestController.showOne(patient.getId());
        return patientResource;
    }

}
