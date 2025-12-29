package com.eduflow.academic.service.implementations;
import com.eduflow.academic.domain.Level;
import com.eduflow.academic.repo.LevelRepository;
import com.eduflow.academic.repo.SemesterRepository;
import com.eduflow.academic.repo.SpecialityRepository;
import com.eduflow.academic.repo.SubjectRepository;
import com.eduflow.academic.service.interfaces.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final LevelRepository levelRepository;
    private final SpecialityRepository specialityRepository;
    private final SemesterRepository semesterRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public Level createLevel(String specialityId, Level level) {
        specialityRepository.findById(specialityId)
                .orElseThrow(() -> new IllegalArgumentException("Speciality not found: " + specialityId));

        level.setSpecialityId(specialityId);
        return levelRepository.save(level);
    }

    @Override
    public List<Level> getLevelsBySpeciality(String specialityId) {
        return levelRepository.findBySpecialityId(specialityId);
    }

    @Override
    public Level getLevel(String id) {
        return levelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Level not found: " + id));
    }

    @Override
    public Level updateLevel(String id, Level level) {
        Level existing = getLevel(id);

        if (level.getSpecialityId() == null) {
            level.setSpecialityId(existing.getSpecialityId());
        } else if (!level.getSpecialityId().equals(existing.getSpecialityId())) {
            specialityRepository.findById(level.getSpecialityId())
                    .orElseThrow(() -> new IllegalArgumentException("New speciality not found: " + level.getSpecialityId()));
        }

        level.setId(existing.getId());
        return levelRepository.save(level);
    }

    @Override
    public void deleteLevel(String id) {
        Level level = getLevel(id);


        levelRepository.delete(level);
    }
}
