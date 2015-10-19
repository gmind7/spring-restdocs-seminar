package io.example.schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Created by gmind on 2015-10-05.
 */
@Transactional(readOnly = true)
public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByDoctorIdAndAppointmentDay(Long doctorId, LocalDate appointmentDay, Pageable pageable);

    Page<Schedule> findByPatientId(Long patientId, Pageable pageable);

}
