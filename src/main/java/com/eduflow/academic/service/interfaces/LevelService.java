package com.eduflow.academic.service.interfaces;

import com.eduflow.academic.domain.Level;

import java.util.List;

public interface LevelService {
    Level createLevel(String specialityId, Level level);
    List<Level> getLevelsBySpeciality(String specialityId);
    Level getLevel(String id);
    Level updateLevel(String id, Level level);
    void deleteLevel(String id);
}