package io.example.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gmind on 2015-10-05.
 */
@Transactional(readOnly = true)
public interface PatientJpaRepository extends JpaRepository<Patient, Long> {
}
