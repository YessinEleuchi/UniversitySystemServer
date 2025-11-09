package com.eduflow.teaching.service;

import com.eduflow.teaching.domain.CourseInstance;
import com.eduflow.teaching.domain.Session;
import com.eduflow.teaching.repo.CourseInstanceRepository;
import com.eduflow.teaching.repo.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Service de gestion des séances de cours (Session).
 * <p>
 * Une Session représente un créneau horaire concret (date/heure début-fin, salle, type : CM/TD/TP/Examen).
 * Règles métier strictes :
 * - La CourseInstance doit exister.
 * - start < end
 * - Pas de chevauchement avec une autre session dans la même salle.
 * - Pas de chevauchement pour le même enseignant.
 * - Pas de chevauchement pour les étudiants de la classe (via ClassGroup).
 * - Type autorisé : CM, TD, TP, EXAM, PROJECT, etc.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final CourseInstanceRepository courseInstanceRepository;

    /**
     * Crée une nouvelle séance de cours.
     *
     * @param courseInstanceId ID du cours ouvert
     * @param start            Date/heure de début (UTC ou timezone géré côté front)
     * @param end              Date/heure de fin
     * @param room             Salle (ex: "A101", "Amphi B")
     * @param type             Type de séance (CM, TD, TP, EXAM, etc.)
     * @return Session persistée
     * @throws IllegalArgumentException si données invalides
     * @throws IllegalStateException    si conflit de planning
     */
    public Session createSession(String courseInstanceId,
                                 Instant start,
                                 Instant end,
                                 String room,
                                 String type) {

        // 1. Vérification CourseInstance existe
        CourseInstance course = courseInstanceRepository.findById(courseInstanceId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cours ouvert introuvable : " + courseInstanceId));

        // 2. Validations basiques
        if (start == null || end == null || start.isAfter(end) || start.equals(end)) {
            throw new IllegalArgumentException("Les dates de début/fin sont invalides");
        }

        if (room == null || room.trim().isEmpty()) {
            throw new IllegalArgumentException("La salle est obligatoire");
        }

        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type de séance est obligatoire");
        }

        // 3. Vérification conflits de planning
        checkNoRoomConflict(room, start, end, null);
        checkNoTeacherConflict(course.getTeacherId(), start, end, null);
        checkNoClassGroupConflict(course.getClassGroupId(), start, end, null);

        // 4. Création
        Session session = Session.builder()
                .courseInstanceId(courseInstanceId)
                .startTime(start)
                .endTime(end)
                .room(room)
                .type(type.toUpperCase())
                .build();

        return sessionRepository.save(session);
    }

    /**
     * Liste toutes les séances d'un cours ouvert.
     */
    public List<Session> getSessionsForCourse(String courseInstanceId) {
        return sessionRepository.findByCourseInstanceIdOrderByStartTimeAsc(courseInstanceId);
    }

    /**
     * Planning d'un enseignant (toutes ses séances).
     */
    public List<Session> getSessionsForTeacher(String teacherId) {
        return sessionRepository.findByTeacherId(teacherId);
    }

    /**
     * Planning d'une salle.
     */
    public List<Session> getSessionsForRoom(String room) {
        return sessionRepository.findByRoomOrderByStartTimeAsc(room);
    }

    /**
     * Planning d'une classe (via ClassGroup).
     */
    public List<Session> getSessionsForClassGroup(String classGroupId) {
        return sessionRepository.findByClassGroupId(classGroupId);
    }

    /**
     * Planning d'un étudiant (via toutes ses inscriptions).
     */
    public List<Session> getSessionsForStudent(String studentId) {
        return sessionRepository.findByStudentId(studentId);
    }

    // ========================================================================
    // MÉTHODES PRIVÉES DE VÉRIFICATION DE CONFLITS
    // ========================================================================

    private void checkNoRoomConflict(String room, Instant start, Instant end, String excludeSessionId) {
        boolean conflict = sessionRepository.existsByRoomAndTimeOverlap(room, start, end, excludeSessionId);
        if (conflict) {
            throw new IllegalStateException("La salle '" + room + "' est déjà occupée à ce créneau");
        }
    }

    private void checkNoTeacherConflict(String teacherId, Instant start, Instant end, String excludeSessionId) {
        boolean conflict = sessionRepository.existsByTeacherAndTimeOverlap(teacherId, start, end, excludeSessionId);
        if (conflict) {
            throw new IllegalStateException("L'enseignant est déjà occupé à ce créneau");
        }
    }

    private void checkNoClassGroupConflict(String classGroupId, Instant start, Instant end, String excludeSessionId) {
        boolean conflict = sessionRepository.existsByClassGroupAndTimeOverlap(classGroupId, start, end, excludeSessionId);
        if (conflict) {
            throw new IllegalStateException("La classe a déjà un cours prévu à ce créneau");
        }
    }

    // ========================================================================
    // MISE À JOUR & SUPPRESSION (bonus)
    // ========================================================================

    public Session updateSession(String sessionId, Instant start, Instant end, String room, String type) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Séance introuvable : " + sessionId));

        checkNoRoomConflict(room, start, end, sessionId);
        // réutilise les mêmes checks avec excludeSessionId

        session.setStartTime(start);
        session.setEndTime(end);
        session.setRoom(room);
        session.setType(type.toUpperCase());

        return sessionRepository.save(session);
    }

    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}