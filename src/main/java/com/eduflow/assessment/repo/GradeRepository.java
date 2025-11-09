package com.eduflow.assessment.repo;

import com.eduflow.assessment.domain.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends MongoRepository<Grade, String> {

    // toutes les notes d’une évaluation
    List<Grade> findByEvaluationId(String evaluationId);

    // toutes les notes d’un étudiant
    List<Grade> findByStudentId(String studentId);

    // vérifier si une note existe déjà pour cet étudiant
    Optional<Grade> findByStudentIdAndEvaluationId(String studentId, String evaluationId);
}
