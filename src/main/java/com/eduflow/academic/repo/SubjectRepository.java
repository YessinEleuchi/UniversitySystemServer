package com.eduflow.academic.repo;

import com.eduflow.academic.domain.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends MongoRepository<Subject, String> {

    // toutes les matières d’un semestre
    List<Subject> findBySemesterId(String semesterId);

    // pour vérifier les doublons de code
    Optional<Subject> findByCode(String code);

    boolean existsByCode(String code);
}
