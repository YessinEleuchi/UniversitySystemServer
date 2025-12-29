package com.eduflow.teaching.repo;

import com.eduflow.teaching.domain.CourseInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourseInstanceRepository extends MongoRepository<CourseInstance, String> {

    Optional<CourseInstance> findByClassGroupIdAndSubjectIdAndSemesterCodeAndAcademicYear(
            String classGroupId, String subjectId, String semesterCode, Integer academicYear
    );
}
