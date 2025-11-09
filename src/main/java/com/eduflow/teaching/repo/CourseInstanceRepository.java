package com.eduflow.teaching.repo;

import com.eduflow.teaching.domain.CourseInstance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;


public interface CourseInstanceRepository extends MongoRepository<CourseInstance, String> {


    /**
     * Tous les cours ouverts pour une classe (emploi du temps de la classe).
     */
    List<CourseInstance> findByClassGroupId(String classGroupId);

    /**
     * Tous les cours assignés à un enseignant (sa charge pédagogique).
     */
    List<CourseInstance> findByTeacherId(String teacherId);

    /**
     * Tous les cours d'un semestre donné (utile pour le planning global).
     */
    List<CourseInstance> findBySemesterId(String semesterId);

    /**
     * Tous les cours d'une année académique.
     */
    List<CourseInstance> findByAcademicYear(Integer academicYear);

    Optional<CourseInstance> findByClassGroupIdAndSubjectIdAndSemesterIdAndAcademicYear(
            String classGroupId,
            String subjectId,
            String semesterId,
            Integer academicYear);

    /**
     * Emploi du temps complet d'une classe pour une année donnée.
     */
    List<CourseInstance> findByClassGroupIdAndAcademicYear(String classGroupId, Integer academicYear);

    /**
     * Tous les cours d'un enseignant dans une année donnée (rapport de service).
     */
    List<CourseInstance> findByTeacherIdAndAcademicYear(String teacherId, Integer academicYear);

    /**
     * Tous les cours d'une matière dans une classe (historique ou suivi).
     */
    List<CourseInstance> findByClassGroupIdAndSubjectId(String classGroupId, String subjectId);
    Long countByTeacherId(String teacherId);

    /**
     * Cours d'une classe dans un semestre précis (semestre S1 ou S2).
     */
    List<CourseInstance> findByClassGroupIdAndSemesterId(String classGroupId, String semesterId);

    /**
     * Cours d'un enseignant dans un semestre donné.
     */
    List<CourseInstance> findByTeacherIdAndSemesterId(String teacherId, String semesterId);

    /**
     * Tous les cours d'une promotion (level) via le levelId de la classGroup.
     * Nécessite une requête custom car pas de jointure directe en Mongo.
     */
    @Query("{ 'classGroupId': { $in: ?0 } }") // ?0 = liste des classGroupIds du level
    List<CourseInstance> findByClassGroupIdsIn(List<String> classGroupIds);

    /**
     * Variante avec année – pour le bilan annuel d’un niveau.
     */
    @Query("{ 'classGroupId': { $in: ?0 }, 'academicYear': ?1 }")
    List<CourseInstance> findByClassGroupIdsInAndAcademicYear(List<String> classGroupIds, Integer academicYear);


    // ========================================================================
    // 5. RECHERCHE PAR SUBJECT + SEMESTER (contrôle programme respecté)
    // ========================================================================

    /**
     * Vérifie que toutes les matières d'un semestre sont bien ouvertes dans une classe.
     */
    List<CourseInstance> findBySemesterIdAndClassGroupId(String semesterId, String classGroupId);



    /**
     * Supprimer tous les cours d'une classe (quand on supprime/archive une classe).
     */
    void deleteByClassGroupId(String classGroupId);

    /**
     * Supprimer tous les cours d'un enseignant (démission, etc.).
     */
    void deleteByTeacherId(String teacherId);
}