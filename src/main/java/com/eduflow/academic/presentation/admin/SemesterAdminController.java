package com.eduflow.academic.presentation.admin;

import com.eduflow.academic.domain.Semester;
import com.eduflow.academic.service.interfaces.LevelService;
import com.eduflow.academic.service.interfaces.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/academic")
@RequiredArgsConstructor
public class SemesterAdminController {

    private final SemesterService semesterService;
    private final LevelService levelService;

    @GetMapping("/levels/{levelId}/semesters")
    public String listByLevel(@PathVariable String levelId, Model model) {
        model.addAttribute("level", levelService.getLevel(levelId));
        model.addAttribute("semesters", semesterService.getSemestersByLevel(levelId));
        return "admin/academic/semesters/list";
    }

    @GetMapping("/levels/{levelId}/semesters/new")
    public String showCreateForm(@PathVariable String levelId, Model model) {
        Semester semester = new Semester();
        semester.setLevelId(levelId);
        model.addAttribute("level", levelService.getLevel(levelId));
        model.addAttribute("semester", semester);
        return "admin/academic/semesters/form";
    }

    @PostMapping("/levels/{levelId}/semesters")
    public String create(@PathVariable String levelId,
                         @ModelAttribute Semester semester) {
        semesterService.createSemester(levelId, semester);
        return "redirect:/admin/academic/levels/" + levelId + "/semesters";
    }

    @GetMapping("/semesters/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Semester semester = semesterService.getSemester(id);
        model.addAttribute("semester", semester);
        model.addAttribute("level", levelService.getLevel(semester.getLevelId()));
        return "admin/academic/semesters/form";
    }

    @PostMapping("/semesters/{id}")
    public String update(@PathVariable String id,
                         @ModelAttribute Semester semester) {
        Semester updated = semesterService.updateSemester(id, semester);
        return "redirect:/admin/academic/levels/" + updated.getLevelId() + "/semesters";
    }

    @PostMapping("/semesters/{id}/delete")
    public String delete(@PathVariable String id) {
        Semester semester = semesterService.getSemester(id);
        String levelId = semester.getLevelId();
        semesterService.deleteSemester(id);
        return "redirect:/admin/academic/levels/" + levelId + "/semesters";
    }
}
