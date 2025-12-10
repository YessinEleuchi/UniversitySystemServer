package com.eduflow.academic.presentation.api;

import com.eduflow.academic.domain.Subject;
import com.eduflow.academic.service.interfaces.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic")
@RequiredArgsConstructor
public class SubjectRestController {

    private final SubjectService subjectService;

    @GetMapping("/semesters/{semesterId}/subjects")
    public List<Subject> getBySemester(@PathVariable String semesterId) {
        return subjectService.getSubjectsBySemester(semesterId);
    }

    @GetMapping("/subjects/{id}")
    public Subject getById(@PathVariable String id) {
        return subjectService.getSubject(id);
    }
}
