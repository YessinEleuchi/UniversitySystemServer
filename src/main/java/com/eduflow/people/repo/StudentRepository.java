package com.eduflow.people.repo;

import com.eduflow.people.domain.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {

    Optional<Student> findByMatricule(String matricule);

    Optional<Student> findByEmail(String email);

    // pour lister les étudiants d'une classe donnée
    List<Student> findByClassGroupId(String classGroupId);
}
