package io.example.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gmind on 2015-10-05.
 */
@Transactional(readOnly = true)
public interface DoctorJpaRepository extends JpaRepository<Doctor, Long> {

}
