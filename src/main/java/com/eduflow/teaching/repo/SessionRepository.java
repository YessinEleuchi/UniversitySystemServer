package com.eduflow.teaching.repo;

import com.eduflow.teaching.domain.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;

public interface SessionRepository extends MongoRepository<Session, String> {

    List<Session> findByCourseInstanceId(String courseInstanceId);

    List<Session> findByCourseInstanceIdOrderByStartTimeAsc(String courseInstanceId);

    // === CONFLITS HORAIRES ===
    @Query("{ 'room': ?0, 'startTime': { $lt: ?2 }, 'endTime': { $gt: ?1 }, '_id': { $ne: ?3 } }")
    boolean existsByRoomAndTimeOverlap(String room, Instant start, Instant end, String excludeId);

    @Query("{ 'teacherId': ?0, 'startTime': { $lt: ?2 }, 'endTime': { $gt: ?1 }, '_id': { $ne: ?3 } }")
    boolean existsByTeacherAndTimeOverlap(String teacherId, Instant start, Instant end, String excludeId);

    @Query("{ 'classGroupId': ?0, 'startTime': { $lt: ?2 }, 'endTime': { $gt: ?1 }, '_id': { $ne: ?3 } }")
    boolean existsByClassGroupAndTimeOverlap(String classGroupId, Instant start, Instant end, String excludeId);

    // === PLANNINGS ===
    @Query("{ 'teacherId': ?0 }")
    List<Session> findByTeacherId(String teacherId);

    List<Session> findByRoomOrderByStartTimeAsc(String room);

    @Query("{ 'classGroupId': ?0 }")
    List<Session> findByClassGroupId(String classGroupId);

}