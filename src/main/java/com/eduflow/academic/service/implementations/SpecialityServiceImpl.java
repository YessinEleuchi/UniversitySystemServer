package com.eduflow.academic.service.implementations;

import com.eduflow.academic.domain.Speciality;
import com.eduflow.academic.repo.CycleRepository;
import com.eduflow.academic.repo.LevelRepository;
import com.eduflow.academic.repo.SpecialityRepository;
import com.eduflow.academic.service.interfaces.SpecialityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialityServiceImpl implements SpecialityService {

    private final SpecialityRepository specialityRepository;
    private final CycleRepository cycleRepository;
    private final LevelRepository levelRepository;

    @Override
    public Speciality createSpeciality(String cycleId, Speciality speciality) {
        cycleRepository.findById(cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found: " + cycleId));

        speciality.setCycleId(cycleId);
        String code = speciality.getCode() == null ? null : speciality.getCode().trim().toUpperCase();
        String label = speciality.getLabel() == null ? null : speciality.getLabel().trim();

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code is required");
        }
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label is required");
        }
        if (specialityRepository.existsByCycleIdAndCodeIgnoreCase(cycleId, code)) {
            throw new IllegalStateException("Speciality code already exists in this cycle: " + code);
        }

        if (specialityRepository.existsByCycleIdAndLabelIgnoreCase(cycleId, label)) {
            throw new IllegalStateException("Speciality label already exists in this cycle: " + label);
        }

        return specialityRepository.save(speciality);
    }

    @Override
    public List<Speciality> getSpecialitiesByCycle(String cycleId) {
        return specialityRepository.findByCycleId(cycleId);
    }

    @Override
    public Speciality getSpeciality(String id) {
        return specialityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Speciality not found: " + id));
    }

    @Override
    public Speciality updateSpeciality(String id, Speciality speciality) {
        Speciality existing = getSpeciality(id);

        if (speciality.getCode() != null
                && !speciality.getCode().equals(existing.getCode())
                && specialityRepository.existsByCode(speciality.getCode())) {
            throw new IllegalStateException("Speciality with code " + speciality.getCode() + " already exists");
        }

        if (speciality.getCycleId() == null) {
            speciality.setCycleId(existing.getCycleId());
        } else if (!speciality.getCycleId().equals(existing.getCycleId())) {
            cycleRepository.findById(speciality.getCycleId())
                    .orElseThrow(() -> new IllegalArgumentException("New cycle not found: " + speciality.getCycleId()));
        }

        speciality.setId(existing.getId());
        return specialityRepository.save(speciality);
    }

    @Override
    public void deleteSpeciality(String id) {
        Speciality speciality = getSpeciality(id);

        if (!levelRepository.findBySpecialityId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete speciality with existing levels");
        }

        specialityRepository.delete(speciality);
    }
}
