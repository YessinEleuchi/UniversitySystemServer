package com.eduflow.teaching.service;

import com.eduflow.academic.repo.LevelRepository;
import com.eduflow.teaching.domain.ClassGroup;
import com.eduflow.teaching.repo.ClassGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassManagementService {

    private final ClassGroupRepository classGroupRepository;
    private final LevelRepository levelRepository;

    // ✅ LIST (pour controller: list page)
    @Transactional(readOnly = true)
    public List<ClassGroup> getAllClassGroups() {
        return classGroupRepository.findAll();
    }

    // ✅ GET ONE (utile si un jour tu veux page details ou pré-remplir autrement)
    @Transactional(readOnly = true)
    public ClassGroup getById(String id) {
        return classGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ClassGroup not found: " + id));
    }

    // ✅ CREATE (tu l’as déjà, je garde ton style)
    public ClassGroup createClassGroup(String levelId, Integer academicYear, String code, String label) {
        if (!levelRepository.existsById(levelId)) {
            throw new IllegalArgumentException("Level with id '" + levelId + "' does not exist");
        }

        classGroupRepository.findByCode(code).ifPresent(cg -> {
            throw new IllegalStateException("ClassGroup with code '" + code + "' already exists");
        });

        ClassGroup cg = ClassGroup.builder()
                .levelId(levelId)
                .academicYear(academicYear)
                .code(code)
                .label(label)
                .build();

        return classGroupRepository.save(cg);
    }

    // ✅ CREATE (surcharge pratique si controller envoie directement un objet)
    public ClassGroup createClassGroup(ClassGroup cg) {
        if (cg.getLevelId() == null || cg.getAcademicYear() == null || cg.getCode() == null) {
            throw new IllegalArgumentException("levelId, academicYear and code are required");
        }
        return createClassGroup(cg.getLevelId(), cg.getAcademicYear(), cg.getCode(), cg.getLabel());
    }

    // ✅ UPDATE (pour controller: POST /{id})
    public ClassGroup updateClassGroup(String id, ClassGroup payload) {
        ClassGroup existing = classGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ClassGroup not found: " + id));

        String newLevelId = payload.getLevelId();
        Integer newAcademicYear = payload.getAcademicYear();
        String newCode = payload.getCode();
        String newLabel = payload.getLabel();

        // validations minimales
        if (newLevelId == null || newAcademicYear == null || newCode == null) {
            throw new IllegalArgumentException("levelId, academicYear and code are required");
        }

        if (!levelRepository.existsById(newLevelId)) {
            throw new IllegalArgumentException("Level with id '" + newLevelId + "' does not exist");
        }

        // unicité code (si changé)
        if (!existing.getCode().equalsIgnoreCase(newCode)) {
            classGroupRepository.findByCode(newCode).ifPresent(other -> {
                if (!other.getId().equals(id)) {
                    throw new IllegalStateException("ClassGroup with code '" + newCode + "' already exists");
                }
            });
        }

        existing.setLevelId(newLevelId);
        existing.setAcademicYear(newAcademicYear);
        existing.setCode(newCode);
        existing.setLabel(newLabel);

        return classGroupRepository.save(existing);
    }

    // ✅ DELETE (pour controller: POST /{id}/delete)
    public void deleteClassGroup(String id) {
        if (!classGroupRepository.existsById(id)) {
            throw new IllegalArgumentException("ClassGroup not found: " + id);
        }
        classGroupRepository.deleteById(id);
    }

    // ✅ LIST par Level + Year (tu l’as déjà)
    @Transactional(readOnly = true)
    public List<ClassGroup> getClassGroupsForLevel(String levelId, Integer academicYear) {
        if (!levelRepository.existsById(levelId)) {
            throw new IllegalArgumentException("Level with id '" + levelId + "' does not exist");
        }
        return classGroupRepository.findByLevelIdAndAcademicYear(levelId, academicYear);
    }
}
