package com.eduflow.teaching.service;

import com.eduflow.academic.domain.Semester;
import com.eduflow.academic.domain.Subject;
import com.eduflow.academic.repo.SemesterRepository;
import com.eduflow.academic.repo.SubjectRepository;
import com.eduflow.people.repo.TeacherRepository;
import com.eduflow.teaching.domain.ClassGroup;
import com.eduflow.teaching.domain.CourseInstance;
import com.eduflow.teaching.repo.ClassGroupRepository;
import com.eduflow.teaching.repo.CourseInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional // Garantit que toutes les vérifications + sauvegarde sont atomiques
public class CourseOpeningService {

    private final CourseInstanceRepository courseInstanceRepository;
    private final ClassGroupRepository classGroupRepository;
    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;
    private final TeacherRepository teacherRepository;

    /**
     * Ouvre une instance de cours (CourseInstance) pour une classe, une matière, un prof et un semestre donné.
     * <p>
     * Règles métier appliquées :
     * - Toutes les entités référencées doivent exister.
     * - Le semestre doit appartenir au même niveau (level) que la classe.
     * - La matière doit être rattachée au semestre indiqué.
     * - Il ne doit pas exister déjà un cours ouvert pour la même combinaison
     *   (classGroup + subject + semester + academicYear) → évite les doublons.
     * - Si academicYear est null, on récupère celui de la ClassGroup.
     *
     * @param subjectId     ID de la matière (Subject)
     * @param classGroupId  ID du groupe de classe
     * @param teacherId     ID de l'enseignant
     * @param semesterId    ID du semestre
     * @param academicYear  Année académique (optionnelle)
     * @return CourseInstance créée et persistée
     */
    public CourseInstance openCourse(String subjectId,
                                     String classGroupId,
                                     String teacherId,
                                     String semesterId,
                                     Integer academicYear) {

        // 1. Vérification et récupération du Subject
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Matière introuvable avec l'ID : " + subjectId));

        // 2. Vérification et récupération du ClassGroup
        ClassGroup classGroup = classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Groupe de classe introuvable avec l'ID : " + classGroupId));

        // 3. Vérification du Teacher
        teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Enseignant introuvable avec l'ID : " + teacherId));

        // 4. Vérification et récupération du Semester
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Semestre introuvable avec l'ID : " + semesterId));

        // 5. Règle métier : le semestre doit appartenir au même niveau que la classe
        if (!semester.getLevelId().equals(classGroup.getLevelId())) {
            throw new IllegalStateException(
                    "Le semestre '" + semester.getLabel() +
                            "' n'appartient pas au niveau de la classe '" + classGroup.getLabel() + "'");
        }

        // 6. Règle métier : la matière doit être rattachée au semestre indiqué
        if (!subject.getSemesterId().equals(semesterId)) {
            throw new IllegalStateException(
                    "La matière '" + subject.getTitle() +
                            "' n'est pas programmée pour le semestre '" + semester.getLabel() + "'");
        }

        // 7. Année académique par défaut = celle de la classe si non fournie
        Integer finalAcademicYear = academicYear != null ? academicYear : classGroup.getAcademicYear();
        if (finalAcademicYear == null) {
            throw new IllegalStateException("Aucune année académique définie pour la classe " + classGroupId);
        }

        // 8. Unicité du cours pour (classGroup + subject + semester + academicYear)
        Optional<CourseInstance> existing = courseInstanceRepository
                .findByClassGroupIdAndSemesterIdAndAcademicYear(
                        classGroupId, semesterId, finalAcademicYear);

        if (existing.isPresent()) {
            throw new IllegalStateException(
                    "Un cours est déjà ouvert pour cette classe, cette matière, ce semestre et cette année académique.");
        }

        // 9. Création de l'instance de cours
        CourseInstance courseInstance = CourseInstance.builder()
                .classGroupId(classGroupId)
                .teacherId(teacherId)
                .semesterId(semesterId)
                .academicYear(finalAcademicYear)
                .build();

        // 10. Persistance
        return courseInstanceRepository.save(courseInstance);
    }
}