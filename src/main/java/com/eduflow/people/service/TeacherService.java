package com.eduflow.people.service;

import com.eduflow.people.domain.Teacher;
import com.eduflow.people.repo.TeacherRepository;
import com.eduflow.teaching.repo.CourseInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des enseignants (création, mise à jour, recherche).
 * <p>
 * Règles métier :
 * - Code unique à vie (ex: PROF001, MATH01)
 * - Email unique (ignore case)
 * - Un enseignant peut changer de département → historique conservé
 * - Recherche full-text sur nom/prénom/code
 * - Charge pédagogique consultable (via CourseInstance)
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseInstanceRepository courseInstanceRepository; // pour charge pédagogique

    /**
     * Crée un nouvel enseignant.
     */
    public Teacher createTeacher(Teacher teacher) {
        validateTeacherForCreation(teacher);

        // Unicité code
        teacherRepository.findByCodeIgnoreCase(teacher.getCode())
                .ifPresent(t -> {
                    throw new IllegalStateException("Un enseignant existe déjà avec le code : " + teacher.getCode());
                });

        // Unicité email (ignore case)
        if (StringUtils.hasText(teacher.getEmail())) {
            teacherRepository.findByEmailIgnoreCase(teacher.getEmail().trim())
                    .ifPresent(t -> {
                        throw new IllegalStateException("Email déjà utilisé : " + teacher.getEmail());
                    });
        }

        return teacherRepository.save(teacher);
    }

    /**
     * Récupère un enseignant par ID.
     */
    public Teacher getTeacher(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Enseignant introuvable : " + id));
    }

    /**
     * Tous les enseignants d'un département, triés par nom.
     */
    public List<Teacher> getTeachersByDepartment(String department) {
        if (!StringUtils.hasText(department)) {
            throw new IllegalArgumentException("Département requis");
        }
        return teacherRepository.findByDepartmentOrderByLastNameAscFirstNameAsc(department);
    }

    /**
     * Change le département d'un enseignant.
     */
    public Teacher updateTeacherDepartment(String teacherId, String newDepartment) {
        Teacher teacher = getTeacher(teacherId);

        if (!StringUtils.hasText(newDepartment)) {
            throw new IllegalArgumentException("Nouveau département requis");
        }

        if (!newDepartment.equals(teacher.getDepartment())) {
            teacher.setDepartment(newDepartment);
        }

        return teacherRepository.save(teacher);
    }

    /**
     * Recherche par code (exact ou partiel).
     */
    public Optional<Teacher> findByCode(String code) {
        return teacherRepository.findByCodeIgnoreCase(code);
    }

    /**
     * Recherche autocomplete nom/prénom.
     */
    public List<Teacher> searchByName(String query) {
        if (query == null || query.length() < 2) {
            return List.of();
        }
        return teacherRepository.findTop10ByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(query, query);
    }

    /**
     * Charge pédagogique (nombre de cours ouverts).
     */
    public long getTeachingLoad(String teacherId) {
        getTeacher(teacherId); // valide existence
        return courseInstanceRepository.countByTeacherId(teacherId);
    }

    /**
     * Tous les enseignants actifs (pour dropdown).
     */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findByOrderByLastNameAsc();
    }

    // ========================================================================
    // VALIDATION INTERNE
    // ========================================================================
    private void validateTeacherForCreation(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("L'enseignant ne peut pas être null");
        }
        if (!StringUtils.hasText(teacher.getCode())) {
            throw new IllegalArgumentException("Le code enseignant est obligatoire");
        }
        if (!teacher.getCode().matches("[A-Z0-9]{3,10}")) {
            throw new IllegalArgumentException("Code invalide (majuscules + chiffres, 3-10 caractères)");
        }
        if (!StringUtils.hasText(teacher.getFirstName()) || !StringUtils.hasText(teacher.getLastName())) {
            throw new IllegalArgumentException("Nom et prénom obligatoires");
        }
    }
}