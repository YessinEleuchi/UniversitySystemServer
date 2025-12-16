package com.eduflow.people.presentation;

import com.eduflow.people.domain.Student;
import com.eduflow.people.service.StudentService;
import com.eduflow.teaching.repo.ClassGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentService studentService;
    private final ClassGroupRepository classGroupRepository;

    // 📄 LIST
    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", studentService.getStudentsByClassGroup(null));
        return "admin/students/list";
    }

    // ➕ FORM CREATE
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("classGroups", classGroupRepository.findAll());
        return "admin/students/form";
    }

    // 💾 SAVE
    @PostMapping
    public String create(@ModelAttribute Student student) {
        studentService.createStudent(student);
        return "redirect:/admin/students";
    }

    // 👁️ VIEW
    @GetMapping("/{id}")
    public String view(@PathVariable String id, Model model) {
        model.addAttribute("student", studentService.getStudent(id));
        return "admin/students/view";
    }

    // 🔄 ASSIGN CLASS
    @PostMapping("/{id}/assign-class")
    public String assignClass(@PathVariable String id,
                              @RequestParam String classGroupId) {
        studentService.assignStudentToClass(id, classGroupId);
        return "redirect:/admin/students/" + id;
    }
}
