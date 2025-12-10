package com.eduflow.academic.service.interfaces;

import com.eduflow.academic.domain.Subject;

import java.util.List;

public interface SubjectService {
    Subject createSubject(String semesterId, Subject subject);
    List<Subject> getSubjectsBySemester(String semesterId);
    Subject getSubject(String id);
    Subject updateSubject(String id, Subject subject);
    void deleteSubject(String id);
}
