package com.eduflow.people.presentation;

import com.eduflow.people.domain.Teacher;
import com.eduflow.people.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/teachers")
@RequiredArgsConstructor
public class AdminTeacherController {

    private final TeacherService teacherService;

    // 📄 LIST
    @GetMapping
    public String list(Model model) {
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "admin/teachers/list";
    }

    // ➕ FORM CREATE
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "admin/teachers/form";
    }

    // 💾 SAVE
    @PostMapping
    public String create(@ModelAttribute Teacher teacher) {
        teacherService.createTeacher(teacher);
        return "redirect:/admin/teachers";
    }

    // 👁️ VIEW
    @GetMapping("/{id}")
    public String view(@PathVariable String id, Model model) {
        model.addAttribute("teacher", teacherService.getTeacher(id));
        model.addAttribute("teachingLoad", teacherService.getTeachingLoad(id));
        return "admin/teachers/view";
    }

    // ✏️ UPDATE DEPARTMENT
    @PostMapping("/{id}/department")
    public String updateDepartment(@PathVariable String id,
                                   @RequestParam String department) {
        teacherService.updateTeacherDepartment(id, department);
        return "redirect:/admin/teachers/" + id;
    }
}
