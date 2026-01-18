package com.eduflow.academic.presentation.controllers;

import com.eduflow.academic.domain.Semester;
import com.eduflow.academic.service.interfaces.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/academic/settings")
@RequiredArgsConstructor
public class SemesterAdminController {

    private final SemesterService semesterService;

    // LIST (global)
    @GetMapping("/semesters")
    public String list(Model model) {
        model.addAttribute("semesters", semesterService.getAllSemesters());
        return "admin/academic/settings/semesters";
    }

    // CREATE (modal)
    @PostMapping("/semesters")
    public String create(@ModelAttribute Semester semester) {
        semesterService.addSemester(semester);
        return "redirect:/admin/academic/settings/semesters";
    }

    // UPDATE (modal) -> update by ID
    @PostMapping("/semesters/{id}")
    public String update(@PathVariable String id,
                         @ModelAttribute Semester semester) {
        semesterService.updateSemester(id, semester);
        return "redirect:/admin/academic/settings/semesters";
    }

    // DELETE -> delete by ID
    @PostMapping("/semesters/{id}/delete")
    public String delete(@PathVariable String id) {
        semesterService.deleteSemester(id);
        return "redirect:/admin/academic/settings/semesters";
    }
}
