package com.eduflow.people.presentation;

import com.eduflow.people.domain.Role;
import com.eduflow.people.service.UserAccountService;
import com.eduflow.people.repo.StudentRepository;
import com.eduflow.people.repo.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserAccountController {

    private final UserAccountService userAccountService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    // 📄 LIST USERS
    @GetMapping
    public String list(Model model) {
        model.addAttribute("managers",
                userAccountService.getAllByRole(Role.ROLE_MANAGER));
        model.addAttribute("students",
                userAccountService.getAllByRole(Role.ROLE_STUDENT));
        model.addAttribute("teachers",
                userAccountService.getAllByRole(Role.ROLE_TEACHER));
        return "admin/users/list";
    }

    // ➕ CREATE MANAGER
    @GetMapping("/managers/new")
    public String newManagerForm() {
        return "admin/users/new-manager";
    }

    @PostMapping("/managers")
    public String createManager(@RequestParam String username,
                                @RequestParam String password) {
        userAccountService.createManagerAccount(
                username,
                password,
                Set.of(Role.ROLE_MANAGER)
        );
        return "redirect:/admin/users";
    }

    // ➕ CREATE STUDENT ACCOUNT
    @GetMapping("/students/new")
    public String newStudentAccountForm(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "admin/users/new-student-account";
    }

    @PostMapping("/students")
    public String createStudentAccount(@RequestParam String studentId,
                                       @RequestParam String username,
                                       @RequestParam String password) {
        userAccountService.createStudentAccount(username, password, studentId);
        return "redirect:/admin/users";
    }

    // ➕ CREATE TEACHER ACCOUNT
    @GetMapping("/teachers/new")
    public String newTeacherAccountForm(Model model) {
        model.addAttribute("teachers", teacherRepository.findAll());
        return "admin/users/new-teacher-account";
    }

    @PostMapping("/teachers")
    public String createTeacherAccount(@RequestParam String teacherId,
                                       @RequestParam String username,
                                       @RequestParam String password) {
        userAccountService.createTeacherAccount(username, password, teacherId);
        return "redirect:/admin/users";
    }

    // 🔒 ENABLE / DISABLE
    @PostMapping("/{username}/enable")
    public String toggleEnabled(@PathVariable String username,
                                @RequestParam boolean enabled) {
        userAccountService.toggleEnabled(username, enabled);
        return "redirect:/admin/users";
    }

    // 🔐 LOCK / UNLOCK
    @PostMapping("/{username}/lock")
    public String toggleLock(@PathVariable String username,
                             @RequestParam boolean locked) {
        userAccountService.toggleLock(username, locked);
        return "redirect:/admin/users";
    }
}
