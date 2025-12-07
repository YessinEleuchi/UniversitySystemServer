package com.eduflow.teaching.repo;

import com.eduflow.teaching.domain.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {

    // ========================================================================
    // 1. UNICITÉ & VÉRIFICATIONS
    // ========================================================================

    Optional<Enrollment> findByStudentIdAndCourseInstanceId(String studentId, String courseInstanceId);

    boolean existsByStudentIdAndCourseInstanceId(String studentId, String courseInstanceId);

    // ========================================================================
    // 2. RECHERCHES PAR COURS (liste des inscrits, effectif)
    // ========================================================================

    List<Enrollment> findByCourseInstanceId(String courseInstanceId);

    long countByCourseInstanceId(String courseInstanceId);

    void deleteByCourseInstanceId(String courseInstanceId); // cascade delete

    // ========================================================================
    // 3. RECHERCHES PAR ÉTUDIANT (bulletin, historique)
    // ========================================================================

    List<Enrollment> findByStudentId(String studentId);

    void deleteByStudentId(String studentId); // désinscription totale


    List<Enrollment> findByAcademicYear(Integer academicYear);

    // Option 2 : si tu ne veux pas modifier le modèle → jointure manuelle
    @Query("{ 'courseInstanceId': { $in: ?0 } }")
    List<Enrollment> findByCourseInstanceIdsIn(List<String> courseInstanceIds);

    /**
     * Recherche par année via CourseInstance (utilisé si academicYear n'est pas dans Enrollment)
     */
    @Query("{ 'courseInstanceId': { $in: { $in: ?0 } } }")
    List<Enrollment> findByCourseInstanceAcademicYear(Integer academicYear);
    // Note : cette méthode nécessite un service qui récupère d'abord les courseInstanceIds de l'année
}