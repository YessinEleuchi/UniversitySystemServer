package com.eduflow.academic.service.implementations;

import com.eduflow.academic.domain.Semester;
import com.eduflow.academic.repo.LevelRepository;
import com.eduflow.academic.repo.SemesterRepository;
import com.eduflow.academic.repo.SubjectRepository;
import com.eduflow.academic.service.interfaces.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;
    private final LevelRepository levelRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public Semester createSemester(String levelId, Semester semester) {
        levelRepository.findById(levelId)
                .orElseThrow(() -> new IllegalArgumentException("Level not found: " + levelId));

        semester.setLevelId(levelId);
        return semesterRepository.save(semester);
    }

    @Override
    public List<Semester> getSemestersByLevel(String levelId) {
        return semesterRepository.findByLevelId(levelId);
    }

    @Override
    public Semester getSemester(String id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Semester not found: " + id));
    }

    @Override
    public Semester updateSemester(String id, Semester semester) {
        Semester existing = getSemester(id);

        if (semester.getLevelId() == null) {
            semester.setLevelId(existing.getLevelId());
        } else if (!semester.getLevelId().equals(existing.getLevelId())) {
            levelRepository.findById(semester.getLevelId())
                    .orElseThrow(() -> new IllegalArgumentException("New level not found: " + semester.getLevelId()));
        }

        semester.setId(existing.getId());
        return semesterRepository.save(semester);
    }

    @Override
    public void deleteSemester(String id) {
        Semester semester = getSemester(id);

        if (!subjectRepository.findBySemesterId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete semester with existing subjects");
        }

        semesterRepository.delete(semester);
    }
}
