package com.eduflow.teaching.service;

import com.eduflow.academic.domain.Subject;
import com.eduflow.academic.service.interfaces.SemesterService;
import com.eduflow.people.repo.TeacherRepository;
import com.eduflow.teaching.domain.ClassGroup;
import com.eduflow.teaching.domain.CourseInstance;
import com.eduflow.teaching.repo.ClassGroupRepository;
import com.eduflow.teaching.repo.CourseInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseInstanceService {

    private final CourseInstanceRepository courseInstanceRepository;
    private final ClassGroupRepository classGroupRepository;
    private final com.eduflow.academic.repo.SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final SemesterService semesterService; // ✅ global config

    private String norm(String s) {
        return s == null ? null : s.trim().toUpperCase();
    }

    /**
     * Ouvre une instance de cours pour une classe, une matière, un prof et un semestre global (S1/S2).
     */
    public CourseInstance openCourse(String subjectId,
                                     String classGroupId,
                                     String teacherId,
                                     String semesterCode,
                                     Integer academicYear) {

        // 1) Subject
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Matière introuvable: " + subjectId));

        // 2) ClassGroup
        ClassGroup classGroup = classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new IllegalArgumentException("Groupe introuvable: " + classGroupId));

        // 3) Teacher
        teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Enseignant introuvable: " + teacherId));

        // 4) SemesterCode global exists (+ optionnel: check active)
        String semCode = norm(semesterCode);
        var sem = semesterService.getSemesterByCode(semCode);
        if (!sem.isActive()) {
            throw new IllegalStateException("Le semestre " + semCode + " est désactivé.");
        }

        // 5) Business rules: subject must belong to same level & same semesterCode
        if (subject.getLevelId() == null || !subject.getLevelId().equals(classGroup.getLevelId())) {
            throw new IllegalStateException(
                    "La matière '" + subject.getTitle() + "' n'appartient pas au niveau de la classe '" + classGroup.getLabel() + "'");
        }

        if (subject.getSemesterCode() == null || !norm(subject.getSemesterCode()).equals(semCode)) {
            throw new IllegalStateException(
                    "La matière '" + subject.getTitle() + "' n'est pas programmée pour le semestre '" + semCode + "'");
        }

        // 6) Academic year default from classGroup
        Integer finalYear = academicYear != null ? academicYear : classGroup.getAcademicYear();
        if (finalYear == null) {
            throw new IllegalStateException("Aucune année académique définie pour la classe " + classGroupId);
        }

        // 7) Uniqueness (classGroup + subject + semester + year)
        courseInstanceRepository
                .findByClassGroupIdAndSubjectIdAndSemesterCodeAndAcademicYear(classGroupId, subjectId, semCode, finalYear)
                .ifPresent(ci -> {
                    throw new IllegalStateException("Un cours est déjà ouvert pour cette classe, matière, semestre et année.");
                });

        // 8) Create
        CourseInstance ci = CourseInstance.builder()
                .classGroupId(classGroupId)
                .teacherId(teacherId)
                .subjectId(subjectId)
                .semesterCode(semCode)
                .academicYear(finalYear)
                .build();

        return courseInstanceRepository.save(ci);
    }
}
