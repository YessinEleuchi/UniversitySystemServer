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

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("student", new Student());
        model.addAttribute("classGroups", classGroupRepository.findAll());
        return "admin/users/students/list";
    }

    @PostMapping
    public String create(@ModelAttribute Student student, Model model) {
        try {
            studentService.createStudent(student);
            return "redirect:/admin/students";
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("student", student);
            model.addAttribute("classGroups", classGroupRepository.findAll());
            model.addAttribute("error", e.getMessage());
            return "admin/users/students/list";
        }
    }


    // 👁️ VIEW
    @GetMapping("/{id}")
    public String view(@PathVariable String id, Model model) {
        model.addAttribute("student", studentService.getStudent(id));
        model.addAttribute("classGroups", classGroupRepository.findAll());
        return "admin/users/students/view";
    }

    // 🔄 ASSIGN CLASS
    @PostMapping("/{id}/assign-class")
    public String assignClass(@PathVariable String id,
                              @RequestParam String classGroupId) {
        studentService.assignStudentToClass(id, classGroupId);
        return "redirect:/admin/students/" + id;
    }
}
