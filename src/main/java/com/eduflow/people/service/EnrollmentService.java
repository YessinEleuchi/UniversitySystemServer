package com.eduflow.people.service;

import com.eduflow.people.domain.Enrollment;
import com.eduflow.people.domain.EnrollmentStatus;
import com.eduflow.people.repo.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Transactional(readOnly = true)
    public Enrollment getEnrollment(String id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment introuvable: " + id));
    }

    @Transactional(readOnly = true)
    public List<Enrollment> listByYearLevel(Integer year, String levelId) {
        return enrollmentRepository.findByAcademicYearAndLevelId(year, levelId);
    }

    public Enrollment enroll(Enrollment e) {
        if (e.getStudentId() == null || e.getStudentId().isBlank())
            throw new IllegalArgumentException("studentId obligatoire");
        if (e.getAcademicYear() == null)
            throw new IllegalArgumentException("academicYear obligatoire");

        if (enrollmentRepository.existsByStudentIdAndAcademicYear(e.getStudentId(), e.getAcademicYear()))
            throw new IllegalStateException("Déjà inscrit pour cette année");

        e.setStatus(EnrollmentStatus.ENROLLED);
        e.setCreatedAt(Instant.now());
        return enrollmentRepository.save(e);
    }

    public Enrollment closeEnrollment(String id) {
        Enrollment e = getEnrollment(id);
        e.setStatus(EnrollmentStatus.CLOSED);
        e.setClosedAt(Instant.now());
        return enrollmentRepository.save(e);
    }
}
