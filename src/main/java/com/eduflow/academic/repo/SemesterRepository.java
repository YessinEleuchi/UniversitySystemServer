package com.eduflow.academic.repo;

import com.eduflow.academic.domain.Semester;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends MongoRepository<Semester, String> {

    // tous les semestres d’un niveau
    List<Semester> findByLevelId(String levelId);

    // ex: code S1 pour tel level
    Optional<Semester> findByLevelIdAndCode(String levelId, String code);
}
