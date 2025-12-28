package com.eduflow.teaching.service;

import com.eduflow.teaching.domain.CourseInstance;
import com.eduflow.teaching.repo.RoomRepository;
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
    private final RoomRepository roomRepository;

    public Session createSession(String courseInstanceId,
                                 Instant start,
                                 Instant end,
                                 String roomId,
                                 String type) {

        CourseInstance course = courseInstanceRepository.findById(courseInstanceId)
                .orElseThrow(() -> new IllegalArgumentException("Cours ouvert introuvable : " + courseInstanceId));

        validateTimeRange(start, end);
        validateRequired(roomId, "La salle (roomId) est obligatoire");
        validateRequired(type, "Le type de séance est obligatoire");

        var room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Salle introuvable : " + roomId));
        if (!room.isActive()) throw new IllegalStateException("Salle désactivée : " + room.getCode());

        String teacherId = course.getTeacherId();
        String classGroupId = course.getClassGroupId();

        validateRequired(teacherId, "CourseInstance sans teacherId");
        validateRequired(classGroupId, "CourseInstance sans classGroupId");

        // ✅ checks via méthodes privées (create => exclude=null)
        checkNoRoomConflict(roomId, start, end, null);
        checkNoTeacherConflict(teacherId, start, end, null);
        checkNoClassGroupConflict(classGroupId, start, end, null);

        Session session = Session.builder()
                .courseInstanceId(courseInstanceId)
                .teacherId(teacherId)
                .classGroupId(classGroupId)
                .roomId(roomId)
                .startTime(start)
                .endTime(end)
                .type(type.trim().toUpperCase())
                .build();

        return sessionRepository.save(session);
    }

    public Session updateSession(String sessionId, Instant start, Instant end, String roomId, String type) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Séance introuvable : " + sessionId));

        validateTimeRange(start, end);
        validateRequired(roomId, "La salle (roomId) est obligatoire");
        validateRequired(type, "Le type de séance est obligatoire");

        var room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Salle introuvable : " + roomId));
        if (!room.isActive()) throw new IllegalStateException("Salle désactivée : " + room.getCode());

        // ✅ update => exclude=sessionId
        checkNoRoomConflict(roomId, start, end, sessionId);
        checkNoTeacherConflict(session.getTeacherId(), start, end, sessionId);
        checkNoClassGroupConflict(session.getClassGroupId(), start, end, sessionId);

        session.setStartTime(start);
        session.setEndTime(end);
        session.setRoomId(roomId);
        session.setType(type.trim().toUpperCase());

        return sessionRepository.save(session);
    }

    public List<Session> getSessionsForRoom(String roomId) {
        return sessionRepository.findByRoomIdOrderByStartTimeAsc(roomId);
    }

    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    // --------------------------------------------------------------------
    // VALIDATIONS
    // --------------------------------------------------------------------
    private static void validateTimeRange(Instant start, Instant end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Les dates de début et de fin sont obligatoires");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("La date de fin doit être strictement après la date de début");
        }
    }

    private static void validateRequired(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    // --------------------------------------------------------------------
    // CONFLICTS
    // --------------------------------------------------------------------
    private void checkNoRoomConflict(String roomId, Instant start, Instant end, String excludeSessionId) {
        boolean conflict = (excludeSessionId == null)
                ? sessionRepository.existsRoomOverlap(roomId, start, end)
                : sessionRepository.existsRoomOverlapExcluding(roomId, start, end, excludeSessionId);

        if (conflict) throw new IllegalStateException("La salle est déjà occupée à ce créneau");
    }

    private void checkNoTeacherConflict(String teacherId, Instant start, Instant end, String excludeSessionId) {
        boolean conflict = (excludeSessionId == null)
                ? sessionRepository.existsTeacherOverlap(teacherId, start, end)
                : sessionRepository.existsTeacherOverlapExcluding(teacherId, start, end, excludeSessionId);

        if (conflict) throw new IllegalStateException("L'enseignant est déjà occupé à ce créneau");
    }

    private void checkNoClassGroupConflict(String classGroupId, Instant start, Instant end, String excludeSessionId) {
        boolean conflict = (excludeSessionId == null)
                ? sessionRepository.existsClassGroupOverlap(classGroupId, start, end)
                : sessionRepository.existsClassGroupOverlapExcluding(classGroupId, start, end, excludeSessionId);

        if (conflict) throw new IllegalStateException("La classe a déjà un cours prévu à ce créneau");
    }
}
