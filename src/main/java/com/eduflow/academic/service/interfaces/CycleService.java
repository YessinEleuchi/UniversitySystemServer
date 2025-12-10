package com.eduflow.academic.service.interfaces;

import com.eduflow.academic.domain.Cycle;

import java.util.List;

public interface CycleService {
    Cycle createCycle(Cycle cycle);
    List<Cycle> getAllCycles();
    Cycle getCycle(String id);
    Cycle updateCycle(String id, Cycle cycle);
    void deleteCycle(String id);
}
