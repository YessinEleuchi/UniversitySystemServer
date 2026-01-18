package com.eduflow.academic.presentation.controllers;

import com.eduflow.academic.domain.Subject;
import com.eduflow.academic.service.interfaces.LevelService;
import com.eduflow.academic.service.interfaces.SemesterService;
import com.eduflow.academic.service.interfaces.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/academic")
@RequiredArgsConstructor
public class SubjectAdminController {

    private final SubjectService subjectService;
    private final SemesterService semesterService; // global config
    private final LevelService levelService;

    private String norm(String s) {
        return s == null ? null : s.trim().toUpperCase();
    }

    // LIST
    @GetMapping("/levels/{levelId}/semesters/{code}/subjects")
    public String list(@PathVariable String levelId,
                       @PathVariable String code,
                       Model model) {

        String semCode = norm(code);

        model.addAttribute("level", levelService.getLevel(levelId));
        model.addAttribute("semester", semesterService.getSemesterByCode(semCode)); // SemesterDef
        model.addAttribute("subjects", subjectService.getSubjects(levelId, semCode));

        return "admin/academic/subjects/list";
    }

    // CREATE FORM
    @GetMapping("/levels/{levelId}/semesters/{code}/subjects/new")
    public String showCreateForm(@PathVariable String levelId,
                                 @PathVariable String code,
                                 Model model) {

        String semCode = norm(code);

        Subject subject = new Subject();
        subject.setLevelId(levelId);
        subject.setSemesterCode(semCode);

        model.addAttribute("level", levelService.getLevel(levelId));
        model.addAttribute("semester", semesterService.getSemesterByCode(semCode));
        model.addAttribute("subject", subject);

        return "admin/academic/subjects/form";
    }

    // CREATE
    @PostMapping("/levels/{levelId}/semesters/{code}/subjects")
    public String create(@PathVariable String levelId,
                         @PathVariable String code,
                         @ModelAttribute Subject subject) {

        String semCode = norm(code);
        subjectService.createSubject(levelId, semCode, subject);

        return "redirect:/admin/academic/levels/" + levelId + "/semesters/" + semCode + "/subjects";
    }

    // EDIT FORM
    @GetMapping("/subjects/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {

        Subject subject = subjectService.getSubject(id);

        String levelId = subject.getLevelId();
        String semCode = norm(subject.getSemesterCode());

        model.addAttribute("level", levelService.getLevel(levelId));
        model.addAttribute("semester", semesterService.getSemesterByCode(semCode));
        model.addAttribute("subject", subject);

        return "admin/academic/subjects/form";
    }

    // UPDATE
    @PostMapping("/subjects/{id}")
    public String update(@PathVariable String id,
                         @ModelAttribute Subject subject) {

        Subject updated = subjectService.updateSubject(id, subject);

        String levelId = updated.getLevelId();
        String semCode = norm(updated.getSemesterCode());

        return "redirect:/admin/academic/levels/" + levelId + "/semesters/" + semCode + "/subjects";
    }

    // DELETE
    @PostMapping("/subjects/{id}/delete")
    public String delete(@PathVariable String id) {

        Subject subject = subjectService.getSubject(id);

        String levelId = subject.getLevelId();
        String semCode = norm(subject.getSemesterCode());

        subjectService.deleteSubject(id);

        return "redirect:/admin/academic/levels/" + levelId + "/semesters/" + semCode + "/subjects";
    }
}
