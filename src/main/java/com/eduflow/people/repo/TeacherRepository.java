package com.eduflow.people.repo;

import com.eduflow.people.domain.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.List;

public interface TeacherRepository extends MongoRepository<Teacher, String> {

    Optional<Teacher> findByCode(String code);

    Optional<Teacher> findByEmail(String email);

    // si tu veux lister par département
    List<Teacher> findByDepartment(String department);
}
