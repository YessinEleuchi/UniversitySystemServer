package com.eduflow.academic.presentation.api;

import com.eduflow.academic.domain.Semester;
import com.eduflow.academic.service.interfaces.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic")
@RequiredArgsConstructor
public class SemesterRestController {

    private final SemesterService semesterService;

    @GetMapping("/levels/{levelId}/semesters")
    public List<Semester> getByLevel(@PathVariable String levelId) {
        return semesterService.getSemestersByLevel(levelId);
    }

    @GetMapping("/semesters/{id}")
    public Semester getById(@PathVariable String id) {
        return semesterService.getSemester(id);
    }




}
