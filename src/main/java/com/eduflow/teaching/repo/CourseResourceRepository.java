package com.eduflow.teaching.repo;

import com.eduflow.teaching.domain.CourseResource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseResourceRepository
        extends MongoRepository<CourseResource, String> {

    List<CourseResource> findByCourseId(String courseId);

    List<CourseResource> findByCourseInstanceId(String courseInstanceId);

    List<CourseResource> findByCourseInstanceIdAndPublishedTrue(String courseInstanceId);
}