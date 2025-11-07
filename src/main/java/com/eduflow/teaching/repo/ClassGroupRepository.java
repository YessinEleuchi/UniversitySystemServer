package com.eduflow.teaching.repo;

import com.eduflow.teaching.domain.ClassGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ClassGroupRepository extends MongoRepository<ClassGroup, String> {

    // toutes les classes d’un niveau pour une année
    List<ClassGroup> findByLevelIdAndAcademicYear(String levelId, Integer academicYear);

    Optional<ClassGroup> findByCode(String code);
}
