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
@Transactional // Recommended for write operations with multiple repo calls
public class ClassManagementService {

    private final ClassGroupRepository classGroupRepository;
    private final LevelRepository levelRepository;

    public ClassGroup createClassGroup(String levelId, Integer academicYear, String code, String label) {
        // 1. Check if Level exists
        if (!levelRepository.existsById(levelId)) {
            throw new IllegalArgumentException("Level with id '" + levelId + "' does not exist");
        }

        // 2. Check uniqueness of ClassGroup code (optionally scoped to academic year or globally)
        classGroupRepository.findByCode(code).ifPresent(cg -> {
            throw new IllegalStateException("ClassGroup with code '" + code + "' already exists");
        });

        // 3. Create and save
        ClassGroup cg = ClassGroup.builder()
                .levelId(levelId)
                .academicYear(academicYear)
                .code(code)
                .label(label)
                .build();

        return classGroupRepository.save(cg);
    }

    public List<ClassGroup> getClassGroupsForLevel(String levelId, Integer academicYear) {
        // Optional: also validate level exists when listing (or handle gracefully)
        if (!levelRepository.existsById(levelId)) {
            throw new IllegalArgumentException("Level with id '" + levelId + "' does not exist");
        }
        return classGroupRepository.findByLevelIdAndAcademicYear(levelId, academicYear);
    }
}