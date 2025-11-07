package com.eduflow.teaching.repo;

import com.eduflow.teaching.domain.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {

    // tous les cours d’un étudiant
    List<Enrollment> findByStudentId(String studentId);

    // tous les étudiants d’un cours
    List<Enrollment> findByCourseInstanceId(String courseInstanceId);

    // vérifier si déjà inscrit
    Optional<Enrollment> findByStudentIdAndCourseInstanceId(String studentId, String courseInstanceId);
}
