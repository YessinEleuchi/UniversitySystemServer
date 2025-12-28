package com.eduflow.teaching.repo;

import com.eduflow.teaching.domain.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;

public interface SessionRepository extends MongoRepository<Session, String> {

    List<Session> findByCourseInstanceIdOrderByStartTimeAsc(String courseInstanceId);
    List<Session> findByRoomIdOrderByStartTimeAsc(String roomId);
    List<Session> findByTeacherIdOrderByStartTimeAsc(String teacherId);
    List<Session> findByClassGroupIdOrderByStartTimeAsc(String classGroupId);

    // -------------------- ROOM --------------------
    @Query(value = "{ 'roomId': ?0, 'startTime': { $lt: ?2 }, 'endTime': { $gt: ?1 } }", exists = true)
    boolean existsRoomOverlap(String roomId, Instant start, Instant end);

    @Query(value = "{ '_id': { $ne: ?3 }, 'roomId': ?0, 'startTime': { $lt: ?2 }, 'endTime': { $gt: ?1 } }", exists = true)
    boolean existsRoomOverlapExcluding(String roomId, Instant start, Instant end, String excludeId);

    // -------------------- TEACHER --------------------
    @Query(value = "{ 'teacherId': ?0, 'startTime': { $lt: ?2 }, 'endTime': { $gt: ?1 } }", exists = true)
    boolean existsTeacherOverlap(String teacherId, Instant start, Instant end);

    @Query(value = "{ '_id': { $ne: ?3 }, 'teacherId': ?0, 'startTime': { $lt: ?2 }, 'endTime': { $gt: ?1 } }", exists = true)
    boolean existsTeacherOverlapExcluding(String teacherId, Instant start, Instant end, String excludeId);

    // -------------------- CLASS GROUP --------------------
    @Query(value = "{ 'classGroupId': ?0, 'startTime': { $lt: ?2 }, 'endTime': { $gt: ?1 } }", exists = true)
    boolean existsClassGroupOverlap(String classGroupId, Instant start, Instant end);

    @Query(value = "{ '_id': { $ne: ?3 }, 'classGroupId': ?0, 'startTime': { $lt: ?2 }, 'endTime': { $gt: ?1 } }", exists = true)
    boolean existsClassGroupOverlapExcluding(String classGroupId, Instant start, Instant end, String excludeId);
}
