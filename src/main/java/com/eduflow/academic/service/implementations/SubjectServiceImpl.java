package com.eduflow.academic.service.implementations;

import com.eduflow.academic.domain.Subject;
import com.eduflow.academic.repo.SemesterRepository;
import com.eduflow.academic.repo.SubjectRepository;
import com.eduflow.academic.service.interfaces.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;

    @Override
    public Subject createSubject(String semesterId, Subject subject) {
        semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("Semester not found: " + semesterId));

        subject.setSemesterId(semesterId);

        if (subject.getCode() != null && subjectRepository.existsByCode(subject.getCode())) {
            throw new IllegalStateException("Subject with code " + subject.getCode() + " already exists");
        }

        return subjectRepository.save(subject);
    }

    @Override
    public List<Subject> getSubjectsBySemester(String semesterId) {
        return subjectRepository.findBySemesterId(semesterId);
    }

    @Override
    public Subject getSubject(String id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + id));
    }

    @Override
    public Subject updateSubject(String id, Subject subject) {
        Subject existing = getSubject(id);

        if (subject.getCode() != null
                && !subject.getCode().equals(existing.getCode())
                && subjectRepository.existsByCode(subject.getCode())) {
            throw new IllegalStateException("Subject with code " + subject.getCode() + " already exists");
        }

        if (subject.getSemesterId() == null) {
            subject.setSemesterId(existing.getSemesterId());
        } else if (!subject.getSemesterId().equals(existing.getSemesterId())) {
            semesterRepository.findById(subject.getSemesterId())
                    .orElseThrow(() -> new IllegalArgumentException("New semester not found: " + subject.getSemesterId()));
        }

        subject.setId(existing.getId());
        return subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(String id) {
        Subject subject = getSubject(id);
        subjectRepository.delete(subject);
    }
}
