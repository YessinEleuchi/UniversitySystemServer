package com.eduflow.academic.presentation.admin;

import com.eduflow.academic.domain.Subject;
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
    private final SemesterService semesterService;

    @GetMapping("/semesters/{semesterId}/subjects")
    public String listBySemester(@PathVariable String semesterId, Model model) {
        model.addAttribute("semester", semesterService.getSemester(semesterId));
        model.addAttribute("subjects", subjectService.getSubjectsBySemester(semesterId));
        return "admin/academic/subjects/list";
    }

    @GetMapping("/semesters/{semesterId}/subjects/new")
    public String showCreateForm(@PathVariable String semesterId, Model model) {
        Subject subject = new Subject();
        subject.setSemesterId(semesterId);
        model.addAttribute("semester", semesterService.getSemester(semesterId));
        model.addAttribute("subject", subject);
        return "admin/academic/subjects/form";
    }

    @PostMapping("/semesters/{semesterId}/subjects")
    public String create(@PathVariable String semesterId,
                         @ModelAttribute Subject subject) {
        subjectService.createSubject(semesterId, subject);
        return "redirect:/admin/academic/semesters/" + semesterId + "/subjects";
    }

    @GetMapping("/subjects/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Subject subject = subjectService.getSubject(id);
        model.addAttribute("subject", subject);
        model.addAttribute("semester", semesterService.getSemester(subject.getSemesterId()));
        return "admin/academic/subjects/form";
    }

    @PostMapping("/subjects/{id}")
    public String update(@PathVariable String id,
                         @ModelAttribute Subject subject) {
        Subject updated = subjectService.updateSubject(id, subject);
        return "redirect:/admin/academic/semesters/" + updated.getSemesterId() + "/subjects";
    }

    @PostMapping("/subjects/{id}/delete")
    public String delete(@PathVariable String id) {
        Subject subject = subjectService.getSubject(id);
        String semesterId = subject.getSemesterId();
        subjectService.deleteSubject(id);
        return "redirect:/admin/academic/semesters/" + semesterId + "/subjects";
    }
}
