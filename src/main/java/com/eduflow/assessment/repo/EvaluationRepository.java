package com.eduflow.assessment.repo;

import com.eduflow.assessment.domain.Evaluation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EvaluationRepository extends MongoRepository<Evaluation, String> {

    // toutes les évaluations d’un cours
    List<Evaluation> findByCourseInstanceId(String courseInstanceId);
}
