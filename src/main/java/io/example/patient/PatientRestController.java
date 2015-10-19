package io.example.patient;

import io.example.config.mapper.AutoMapper;
import io.example.schedule.Schedule;
import io.example.schedule.ScheduleJpaRepository;
import io.example.schedule.ScheduleResourceAssembler;
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

import static io.example.patient.PatientResourceAssembler.PatientResource;
import static io.example.schedule.ScheduleResourceAssembler.ScheduleResource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by gmind on 2015-10-05.
 */
@RestController
@RequestMapping(value = "/patients")
public class PatientRestController {

    @Autowired
    private PatientJpaRepository patientJpaRepository;

    @Autowired
    private ScheduleJpaRepository scheduleJpaRepository;

    @Autowired
    private PatientResourceAssembler patientResourceAssembler;

    @Autowired
    private ScheduleResourceAssembler scheduleResourceAssembler;

    @Autowired
    private PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    private AutoMapper autoMapper;

    @RequestMapping(method = RequestMethod.GET)
    public PagedResources<PatientResource> showAll(@PageableDefault Pageable pageable) {
        Page<Patient> patients = this.patientJpaRepository.findAll(pageable);
        return this.pagedResourcesAssembler.toResource(patients, patientResourceAssembler);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resource<Patient> showOne(@PathVariable("id") Long id) {
        Patient entity = this.patientJpaRepository.findOne(id);
        return this.patientResourceAssembler.toResource(entity);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT)
    public HttpHeaders create(@RequestBody PatientInput patientInput) {
        Patient mapping = this.autoMapper.map(patientInput, Patient.class);
        Patient entity  = this.patientJpaRepository.save(mapping);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(linkTo(PatientRestController.class).slash(entity.getId()).toUri());
        return httpHeaders;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public void update(@PathVariable("id") Long id, @RequestBody PatientInput patientInput) {
        Patient source  = this.autoMapper.map(patientInput, Patient.class);
        Patient target  = this.patientJpaRepository.findOne(id);
        Patient mapping = this.autoMapper.map(source, target, Patient.class);
        this.patientJpaRepository.save(mapping);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void  delete(@PathVariable("id") Long id) {
        this.patientJpaRepository.delete(id);
    }

    @RequestMapping(value = "/{id}/schedules", method = RequestMethod.GET)
    public PagedResources<ScheduleResource> showSchdules(@PathVariable("id") Long id, @PageableDefault Pageable pageable) {
        Page<Schedule> schedules = this.scheduleJpaRepository.findByPatientId(id, pageable);
        return this.pagedResourcesAssembler.toResource(schedules, scheduleResourceAssembler);
    }

}
