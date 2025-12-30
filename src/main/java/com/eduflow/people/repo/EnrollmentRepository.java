package com.eduflow.people.repo;

import com.eduflow.people.domain.Enrollment;
import com.eduflow.people.domain.EnrollmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {

    Optional<Enrollment> findByStudentIdAndAcademicYear(String studentId, Integer academicYear);

    List<Enrollment> findByAcademicYearAndLevelId(Integer academicYear, String levelId);

    List<Enrollment> findByAcademicYearAndLevelIdAndStatus(Integer academicYear, String levelId, EnrollmentStatus status);

    List<Enrollment> findByAcademicYearAndClassGroupId(Integer academicYear, String classGroupId);

    boolean existsByStudentIdAndAcademicYear(String studentId, Integer academicYear);
}
