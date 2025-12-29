package com.eduflow.academic.repo;

import com.eduflow.academic.domain.Semester;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends MongoRepository<Semester, String> {
    Optional<Semester> findByCode(String code);

    boolean existsByCode(String code);

    List<Semester> findAllByOrderByOrderAsc();


    List<Semester> findByActiveTrueOrderByOrderAsc(); // utile partout
}
