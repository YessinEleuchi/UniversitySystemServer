package com.eduflow.academic.repo;

import com.eduflow.academic.domain.Speciality;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SpecialityRepository extends MongoRepository<Speciality, String> {

    // toutes les spécialités d’un cycle
    List<Speciality> findByCycleId(String cycleId);

    Optional<Speciality> findByCode(String code);

    boolean existsByCode(String code);
}
