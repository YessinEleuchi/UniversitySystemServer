package com.eduflow.teaching.service;

import com.eduflow.people.repo.StudentRepository;
import com.eduflow.teaching.domain.CourseInstance;
import com.eduflow.teaching.domain.Enrollment;
import com.eduflow.teaching.repo.CourseInstanceRepository;
import com.eduflow.teaching.repo.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Service complet de gestion des inscriptions étudiants ↔ cours ouverts.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseInstanceRepository courseInstanceRepository;

    // ========================================================================
    // INSCRIPTION
    // ========================================================================
    public Enrollment enrollStudent(String studentId, String courseInstanceId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant introuvable : " + studentId));

        courseInstanceRepository.findById(courseInstanceId)
                .orElseThrow(() -> new IllegalArgumentException("Cours introuvable : " + courseInstanceId));

        enrollmentRepository.findByStudentIdAndCourseInstanceId(studentId, courseInstanceId)
                .ifPresent(e -> {
                    throw new IllegalStateException("L'étudiant est déjà inscrit à ce cours");
                });

        Enrollment enrollment = Enrollment.builder()
                .studentId(studentId)
                .courseInstanceId(courseInstanceId)
                .enrolledAt(Instant.now())
                .build();

        return enrollmentRepository.save(enrollment);
    }

    // ========================================================================
    // RECHERCHES
    // ========================================================================
    public List<Enrollment> getEnrollmentsForCourse(String courseInstanceId) {
        return enrollmentRepository.findByCourseInstanceId(courseInstanceId);
    }

    public List<Enrollment> getEnrollmentsForStudent(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public long countEnrollmentsForCourse(String courseInstanceId) {
        return enrollmentRepository.countByCourseInstanceId(courseInstanceId);
    }

    public boolean isEnrolled(String studentId, String courseInstanceId) {
        return enrollmentRepository.existsByStudentIdAndCourseInstanceId(studentId, courseInstanceId);
    }

    // ========================================================================
    // DÉSINSCRIPTION
    // ========================================================================
    public void unenrollStudent(String studentId, String courseInstanceId) {
        Enrollment enrollment = enrollmentRepository
                .findByStudentIdAndCourseInstanceId(studentId, courseInstanceId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Inscription introuvable pour cet étudiant et ce cours"));

        enrollmentRepository.delete(enrollment);
    }

    // ========================================================================
    // PAR ANNÉE (deux implémentations possibles)
    // ========================================================================
    public List<Enrollment> getEnrollmentsByAcademicYear(Integer academicYear) {
        // Option 1 : si academicYear est dans Enrollment → direct
        // return enrollmentRepository.findByAcademicYear(academicYear);

        // Option 2 : sinon, on passe par CourseInstance
        List<String> courseIds = courseInstanceRepository
                .findByAcademicYear(academicYear)
                .stream()
                .map(CourseInstance::getId)
                .toList();

        return enrollmentRepository.findByCourseInstanceIdsIn(courseIds);
    }

    // ========================================================================
    // BONUS : Inscriptions par classe (via ClassGroupId du CourseInstance)
    // ========================================================================
    public List<Enrollment> getEnrollmentsByClassGroup(String classGroupId) {
        List<String> courseIds = courseInstanceRepository
                .findByClassGroupId(classGroupId)
                .stream()
                .map(CourseInstance::getId)
                .toList();

        return enrollmentRepository.findByCourseInstanceIdsIn(courseIds);
    }

    public long countStudentsInClassGroup(String classGroupId) {
        return getEnrollmentsByClassGroup(classGroupId)
                .stream()
                .map(Enrollment::getStudentId)
                .distinct()
                .count();
    }
}