package com.eduflow.teaching.repo;

import com.eduflow.teaching.domain.CourseInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseInstanceRepository extends MongoRepository<CourseInstance, String> {

    // tous les cours ouverts pour une classe
    List<CourseInstance> findByClassGroupId(String classGroupId);

    // tous les cours d’un enseignant
    List<CourseInstance> findByTeacherId(String teacherId);

    // filtrer par semestre
    List<CourseInstance> findBySemesterId(String semesterId);

    // pour une année donnée
    List<CourseInstance> findByAcademicYear(Integer academicYear);
}
