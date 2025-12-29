package com.eduflow.academic.repo;

import com.eduflow.academic.domain.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends MongoRepository<Subject, String> {

    List<Subject> findByLevelIdAndSemesterCode(String levelId, String semesterCode);

    Optional<Subject> findByLevelIdAndSemesterCodeAndCode(String levelId, String semesterCode, String code);

    boolean existsByLevelIdAndSemesterCodeAndCode(String levelId, String semesterCode, String code);

    String semesterCode(String semesterCode);
}
