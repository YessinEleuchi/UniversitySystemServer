package com.eduflow.assessment.repo;

import com.eduflow.assessment.domain.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends MongoRepository<Grade, String> {

    // 🔹 Toutes les notes d’une évaluation
    List<Grade> findByEvaluationId(String evaluationId);

    // 🔹 Toutes les notes d’un étudiant (via Enrollment)
    List<Grade> findByEnrollmentId(String enrollmentId);

    // 🔹 Vérifier si une note existe déjà pour une évaluation donnée
    Optional<Grade> findByEnrollmentIdAndEvaluationId(
            String enrollmentId,
            String evaluationId
    );
}
