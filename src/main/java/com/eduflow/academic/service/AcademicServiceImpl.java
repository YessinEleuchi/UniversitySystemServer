package com.eduflow.academic.service;

import com.eduflow.academic.domain.*;
import com.eduflow.academic.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicServiceImpl implements AcademicService {

    private final CycleRepository cycleRepository;
    private final SpecialityRepository specialityRepository;
    private final LevelRepository levelRepository;
    private final SemesterRepository semesterRepository;
    private final SubjectRepository subjectRepository;

    // --------------------
    // CYCLES
    // --------------------
    @Override
    public Cycle createCycle(Cycle cycle) {
        // petite validation rapide
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

    // --------------------
    // SPECIALITIES
    // --------------------
    @Override
    public Speciality createSpeciality(String cycleId, Speciality speciality) {
        // vérifier que le cycle existe
        cycleRepository.findById(cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found: " + cycleId));

        speciality.setCycleId(cycleId);

        if (speciality.getCode() != null && specialityRepository.existsByCode(speciality.getCode())) {
            throw new IllegalStateException("Speciality with code " + speciality.getCode() + " already exists");
        }

        return specialityRepository.save(speciality);
    }

    @Override
    public List<Speciality> getSpecialitiesByCycle(String cycleId) {
        return specialityRepository.findByCycleId(cycleId);
    }

    // --------------------
    // LEVELS
    // --------------------
    @Override
    public Level createLevel(String specialityId, Level level) {
        // vérifier que la spécialité existe
        specialityRepository.findById(specialityId)
                .orElseThrow(() -> new IllegalArgumentException("Speciality not found: " + specialityId));

        level.setSpecialityId(specialityId);
        return levelRepository.save(level);
    }

    @Override
    public List<Level> getLevelsBySpeciality(String specialityId) {
        return levelRepository.findBySpecialityId(specialityId);
    }

    // --------------------
    // SEMESTERS
    // --------------------
    @Override
    public Semester createSemester(String levelId, Semester semester) {
        // vérifier que le level existe
        levelRepository.findById(levelId)
                .orElseThrow(() -> new IllegalArgumentException("Level not found: " + levelId));

        semester.setLevelId(levelId);
        return semesterRepository.save(semester);
    }

    @Override
    public List<Semester> getSemestersByLevel(String levelId) {
        return semesterRepository.findByLevelId(levelId);
    }

    // --------------------
    // SUBJECTS
    // --------------------
    @Override
    public Subject createSubject(String semesterId, Subject subject) {
        // vérifier que le semestre existe
        semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("Semester not found: " + semesterId));

        subject.setSemesterId(semesterId);

        // vérifier doublon code
        if (subject.getCode() != null && subjectRepository.existsByCode(subject.getCode())) {
            throw new IllegalStateException("Subject with code " + subject.getCode() + " already exists");
        }

        return subjectRepository.save(subject);
    }

    @Override
    public List<Subject> getSubjectsBySemester(String semesterId) {
        return subjectRepository.findBySemesterId(semesterId);
    }
}
