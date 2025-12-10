package com.eduflow.academic.service.interfaces;

import com.eduflow.academic.domain.Speciality;

import java.util.List;

public interface SpecialityService {
    Speciality createSpeciality(String cycleId, Speciality speciality);
    List<Speciality> getSpecialitiesByCycle(String cycleId);
    Speciality getSpeciality(String id);
    Speciality updateSpeciality(String id, Speciality speciality);
    void deleteSpeciality(String id);
}