package com.eduflow.academic.service.Implementations;


import com.eduflow.academic.domain.Cycle;
import com.eduflow.academic.repo.CycleRepository;
import com.eduflow.academic.repo.SpecialityRepository;
import com.eduflow.academic.service.interfaces.CycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CycleServiceImpl implements CycleService {

    private final CycleRepository cycleRepository;
    private final SpecialityRepository specialityRepository;

    @Override
    public Cycle createCycle(Cycle cycle) {
        if (!StringUtils.hasText(cycle.getCode())) {
            throw new IllegalArgumentException("Cycle code is required");
        }
        if (cycleRepository.existsByCode(cycle.getCode())) {
            throw new IllegalStateException("Cycle with code " + cycle.getCode() + " already exists");
        }
        return cycleRepository.save(cycle);
    }

    @Override
    public List<Cycle> getAllCycles() {
        return cycleRepository.findAll();
    }

    @Override
    public Cycle getCycle(String id) {
        return cycleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found: " + id));
    }

    @Override
    public Cycle updateCycle(String id, Cycle cycle) {
        Cycle existing = getCycle(id);

        if (StringUtils.hasText(cycle.getCode())
                && !cycle.getCode().equals(existing.getCode())
                && cycleRepository.existsByCode(cycle.getCode())) {
            throw new IllegalStateException("Cycle with code " + cycle.getCode() + " already exists");
        }

        cycle.setId(existing.getId());
        if (cycle.getLabel() == null) {
            cycle.setLabel(existing.getLabel());
        }

        return cycleRepository.save(cycle);
    }

    @Override
    public void deleteCycle(String id) {
        Cycle cycle = getCycle(id);

        // protect parent deletion
        if (!specialityRepository.findByCycleId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete cycle with existing specialities");
        }

        cycleRepository.delete(cycle);
    }
}
