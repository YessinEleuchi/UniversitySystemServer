package com.eduflow.academic.presentation.admin;

import com.eduflow.academic.service.interfaces.LevelService;
import com.eduflow.academic.service.interfaces.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/academic")
@RequiredArgsConstructor
public class LevelSemesterController {

    private final LevelService levelService;
    private final SemesterService semesterService;

    @GetMapping("/levels/{levelId}/semesters")
    public String list(@PathVariable String levelId, Model model) {
        model.addAttribute("level", levelService.getLevel(levelId));
        model.addAttribute("semesters", semesterService.getActiveSemesters()); // GLOBAL
        return "admin/academic/semesters/list";
    }
}
