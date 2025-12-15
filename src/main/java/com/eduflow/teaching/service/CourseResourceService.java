package com.eduflow.teaching.service;

import com.eduflow.teaching.domain.CourseResource;
import com.eduflow.teaching.repo.CourseResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseResourceService {

    private final CourseResourceRepository repository;

    public CourseResource addResource(CourseResource resource) {
        return repository.save(resource);
    }

    public List<CourseResource> getCourseResources(String courseId) {
        return repository.findByCourseId(courseId);
    }

    public List<CourseResource> getInstanceResources(String instanceId) {
        return repository.findByCourseInstanceId(instanceId);
    }

    public List<CourseResource> getPublishedResources(String instanceId) {
        return repository.findByCourseInstanceIdAndPublishedTrue(instanceId);
    }
}
