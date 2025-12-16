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

    // 📄 LIST (ONE PAGE: list + modals)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("teacher", new Teacher()); // ✅ required for create modal form
        return "admin/users/teachers/list";
    }

    // 💾 SAVE (Create Teacher)
    @PostMapping
    public String create(@ModelAttribute Teacher teacher) {
        teacherService.createTeacher(teacher);
        return "redirect:/admin/teachers";
    }

    // ✏️ UPDATE DEPARTMENT (from view modal)
    @PostMapping("/{id}/department")
    public String updateDepartment(@PathVariable String id,
                                   @RequestParam String department) {
        teacherService.updateTeacherDepartment(id, department);
        return "redirect:/admin/teachers"; // ✅ back to list page (since view is modal)
    }
}
