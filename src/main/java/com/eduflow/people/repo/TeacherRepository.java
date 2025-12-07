package com.eduflow.people.repo;

import com.eduflow.people.domain.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.List;

public interface TeacherRepository extends MongoRepository<Teacher, String> {

    Optional<Teacher> findByCodeIgnoreCase(String code);

    Optional<Teacher> findByEmailIgnoreCase(String email);

    List<Teacher> findByDepartmentOrderByLastNameAscFirstNameAsc(String department);

    List<Teacher> findTop10ByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(String lastName, String firstName);

    List<Teacher> findByOrderByLastNameAsc();

    long countByDepartment(String department);
}
