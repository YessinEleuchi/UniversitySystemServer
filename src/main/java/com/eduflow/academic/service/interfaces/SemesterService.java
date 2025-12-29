package com.eduflow.academic.service.interfaces;

import com.eduflow.academic.domain.Semester;

import java.util.List;

public interface SemesterService {

    List<Semester> getAllSemesters();

    List<Semester> getActiveSemesters();

    Semester addSemester(Semester semester);

    Semester updateSemester(String id, Semester updated);

    Semester getSemesterById(String id);

    Semester getSemesterByCode(String code);

    void deleteSemester(String id);
}
