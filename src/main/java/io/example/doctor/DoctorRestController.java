package io.example.doctor;

import io.example.config.mapper.AutoMapper;
import io.example.schedule.Schedule;
import io.example.schedule.ScheduleJpaRepository;
import io.example.schedule.ScheduleResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static io.example.doctor.DoctorResourceAssembler.DoctorResource;
import static io.example.schedule.ScheduleResourceAssembler.ScheduleResource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by gmind on 2015-10-05.
 */
@RestController
@RequestMapping(value = "/doctors")
public class DoctorRestController {

    @Autowired
    private DoctorJpaRepository doctorJpaRepository;

    @Autowired
    private ScheduleJpaRepository scheduleJpaRepository;

    @Autowired
    private DoctorResourceAssembler doctorResourceAssembler;

    @Autowired
    private ScheduleResourceAssembler scheduleResourceAssembler;

    @Autowired
    private PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    private AutoMapper autoMapper;

    @RequestMapping(method = RequestMethod.GET)
    public PagedResources<DoctorResource> showAll(@PageableDefault Pageable pageable) {
        Page<Doctor> doctors = this.doctorJpaRepository.findAll(pageable);
        return this.pagedResourcesAssembler.toResource(doctors, doctorResourceAssembler);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resource<Doctor> showOne(@PathVariable("id") Long id) {
        Doctor entity = this.doctorJpaRepository.findOne(id);
        return this.doctorResourceAssembler.toResource(entity);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.PUT)
    public HttpHeaders create(@RequestBody DoctorInput doctorInput) {
        Doctor mapping = this.autoMapper.map(doctorInput, Doctor.class);
        Doctor entity  = this.doctorJpaRepository.save(mapping);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(linkTo(DoctorRestController.class).slash(entity.getId()).toUri());
        return httpHeaders;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public void update(@PathVariable("id") Long id, @RequestBody DoctorInput doctorInput) {
        Doctor source  = this.autoMapper.map(doctorInput, Doctor.class);
        Doctor target  = this.doctorJpaRepository.findOne(id);
        Doctor mapping = this.autoMapper.map(source, target, Doctor.class);
        this.doctorJpaRepository.save(mapping);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void  delete(@PathVariable("id") Long id) {
        this.doctorJpaRepository.delete(id);
    }

    @RequestMapping(value = "/{id}/schedules", method = RequestMethod.GET)
    public PagedResources<ScheduleResource> showSchdules(@PathVariable("id") Long id, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDay, @PageableDefault(size = 50) Pageable pageable) {
        appointmentDay = (appointmentDay==null) ? LocalDateTime.now().toLocalDate() : appointmentDay;
        Page<Schedule> schedules = this.scheduleJpaRepository.findByDoctorIdAndAppointmentDay(id, appointmentDay, pageable);
        return this.pagedResourcesAssembler.toResource(schedules, scheduleResourceAssembler);
    }

}
