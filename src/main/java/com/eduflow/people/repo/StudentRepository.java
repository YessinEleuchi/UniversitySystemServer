package com.eduflow.people.repo;

import com.eduflow.people.domain.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {

    Optional<Student> findByMatriculeIgnoreCase(String matricule);

    Optional<Student> findByEmailIgnoreCase(String email);

    // pour lister les étudiants d'une classe donnée (tri utile pour UI)
    List<Student> findByClassGroupIdOrderByLastNameAscFirstNameAsc(String classGroupId);
    List<Student> findAllByOrderByLastNameAscFirstNameAsc();


    List<Student> findByOrderByLastNameAscFirstNameAsc();


}
