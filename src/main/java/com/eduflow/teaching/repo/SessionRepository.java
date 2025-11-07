package com.eduflow.teaching.repo;

import com.eduflow.teaching.domain.Session;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface SessionRepository extends MongoRepository<Session, String> {

    List<Session> findByCourseInstanceId(String courseInstanceId);

    // pour récupérer les séances à venir
    List<Session> findByCourseInstanceIdAndStartTimeAfter(String courseInstanceId, Instant date);
}
