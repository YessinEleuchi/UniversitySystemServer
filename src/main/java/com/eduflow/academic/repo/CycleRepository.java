package com.eduflow.academic.repo;

import com.eduflow.academic.domain.Cycle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CycleRepository extends MongoRepository<Cycle, String> {

    // pour retrouver un cycle par code (ex: INGENIEUR)
    Optional<Cycle> findByCode(String code);

    boolean existsByCode(String code);
}
