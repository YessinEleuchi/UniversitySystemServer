package com.eduflow.academic.service.interfaces;

import com.eduflow.academic.domain.Semester;

import java.util.List;

public interface SemesterService {
    Semester createSemester(String levelId, Semester semester);
    List<Semester> getSemestersByLevel(String levelId);
    Semester getSemester(String id);
    Semester updateSemester(String id, Semester semester);
    void deleteSemester(String id);
}
