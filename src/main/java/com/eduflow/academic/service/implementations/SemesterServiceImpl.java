package com.eduflow.academic.service.implementations;

import com.eduflow.academic.domain.Semester;
import com.eduflow.academic.repo.SemesterRepository;
import com.eduflow.academic.service.interfaces.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;

    private String normalizeCode(String code) {
        if (code == null) throw new IllegalArgumentException("Code semestre requis");
        String c = code.trim().toUpperCase();
        if (c.isEmpty()) throw new IllegalArgumentException("Code semestre requis");
        return c;
    }

    private void validate(Semester s) {
        if (s.getLabel() == null || s.getLabel().trim().isEmpty()) {
            throw new IllegalArgumentException("Label requis");
        }
        if (s.getOrder() <= 0) {
            throw new IllegalArgumentException("Order doit être >= 1");
        }
        s.setCode(normalizeCode(s.getCode()));
        s.setLabel(s.getLabel().trim());
    }

    @Override
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAllByOrderByOrderAsc();
    }

    @Override
    public List<Semester> getActiveSemesters() {
        return semesterRepository.findByActiveTrueOrderByOrderAsc();
    }

    @Override
    public Semester addSemester(Semester semester) {
        validate(semester);

        if (semesterRepository.existsByCode(semester.getCode())) {
            throw new IllegalArgumentException("Code déjà utilisé : " + semester.getCode());
        }
        return semesterRepository.save(semester);
    }

    @Override
    public Semester updateSemester(String id, Semester updated) {
        Semester existing = getSemesterById(id);
        validate(updated);

        if (!existing.getCode().equals(updated.getCode())
                && semesterRepository.existsByCode(updated.getCode())) {
            throw new IllegalArgumentException("Code déjà utilisé : " + updated.getCode());
        }

        updated.setId(id);
        return semesterRepository.save(updated);
    }

    @Override
    public Semester getSemesterById(String id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Semestre non trouvé : " + id));
    }

    @Override
    public Semester getSemesterByCode(String code) {
        String c = normalizeCode(code);
        return semesterRepository.findByCode(c)
                .orElseThrow(() -> new IllegalArgumentException("Semestre non trouvé : " + c));
    }

    @Override
    public void deleteSemester(String id) {
        semesterRepository.deleteById(id);
    }
}
