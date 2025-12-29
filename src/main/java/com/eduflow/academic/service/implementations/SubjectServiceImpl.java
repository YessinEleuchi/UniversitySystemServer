package com.eduflow.academic.service.implementations;

import com.eduflow.academic.domain.Subject;
import com.eduflow.academic.repo.SubjectRepository;
import com.eduflow.academic.service.interfaces.LevelService;
import com.eduflow.academic.service.interfaces.SemesterService;
import com.eduflow.academic.service.interfaces.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SemesterService semesterService; // config globale
    private final LevelService levelService;       // pour valider levelId

    private String normalizeCode(String v) {
        return v == null ? null : v.trim().toUpperCase();
    }

    @Override
    public Subject createSubject(String levelId, String semesterCode, Subject subject) {
        // 1) validate level exists
        levelService.getLevel(levelId); // throw si absent

        // 2) validate semester exists in global config (et éventuellement active)
        String semCode = normalizeCode(semesterCode);
        semesterService.getSemesterByCode(semCode); // throw si absent (ou check active dans ton service)

        // 3) normalize + assign
        subject.setLevelId(levelId);
        subject.setSemesterCode(semCode);
        subject.setCode(normalizeCode(subject.getCode()));

        // 4) uniqueness within (levelId, semesterCode, code)
        if (subject.getCode() != null
                && subjectRepository.existsByLevelIdAndSemesterCodeAndCode(levelId, semCode, subject.getCode())) {
            throw new IllegalStateException("Subject with code " + subject.getCode()
                    + " already exists for " + levelId + " / " + semCode);
        }

        return subjectRepository.save(subject);
    }

    @Override
    public List<Subject> getSubjects(String levelId, String semesterCode) {
        String semCode = normalizeCode(semesterCode);
        return subjectRepository.findByLevelIdAndSemesterCode(levelId, semCode);
    }

    @Override
    public Subject getSubject(String id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + id));
    }

    @Override
    public Subject updateSubject(String id, Subject patch) {
        Subject existing = getSubject(id);

        // On conserve le scope (levelId + semesterCode) sauf si tu veux permettre move
        String levelId = existing.getLevelId();
        String semCode = existing.getSemesterCode();

        String newCode = normalizeCode(patch.getCode());
        if (newCode != null && !newCode.equals(existing.getCode())) {
            if (subjectRepository.existsByLevelIdAndSemesterCodeAndCode(levelId, semCode, newCode)) {
                throw new IllegalStateException("Subject with code " + newCode + " already exists for this semester");
            }
            existing.setCode(newCode);
        }

        if (patch.getTitle() != null) existing.setTitle(patch.getTitle().trim());
        if (patch.getCredits() != null) existing.setCredits(patch.getCredits());
        if (patch.getCoefficient() != null) existing.setCoefficient(patch.getCoefficient());

        return subjectRepository.save(existing);
    }

    @Override
    public void deleteSubject(String id) {
        Subject subject = getSubject(id);
        subjectRepository.delete(subject);
    }
}
