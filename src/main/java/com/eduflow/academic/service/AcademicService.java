package com.eduflow.academic.service;

import com.eduflow.academic.domain.*;

import java.util.List;

public interface AcademicService {

    // CYCLES
    Cycle createCycle(Cycle cycle);
    List<Cycle> getAllCycles();
    Cycle getCycle(String id);

    // SPECIALITIES
    Speciality createSpeciality(String cycleId, Speciality speciality);
    List<Speciality> getSpecialitiesByCycle(String cycleId);

    // LEVELS
    Level createLevel(String specialityId, Level level);
    List<Level> getLevelsBySpeciality(String specialityId);

    // SEMESTERS
    Semester createSemester(String levelId, Semester semester);
    List<Semester> getSemestersByLevel(String levelId);

    // SUBJECTS
    Subject createSubject(String semesterId, Subject subject);
    List<Subject> getSubjectsBySemester(String semesterId);
}
