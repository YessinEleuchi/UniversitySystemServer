package com.eduflow.people.presentation;

import com.eduflow.people.domain.Role;
import com.eduflow.people.repo.StudentRepository;
import com.eduflow.people.repo.TeacherRepository;
import com.eduflow.people.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("managers", userAccountService.getAllByRole(Role.ROLE_MANAGER));
        model.addAttribute("students", userAccountService.getAllByRole(Role.ROLE_STUDENT));
        model.addAttribute("teachers", userAccountService.getAllByRole(Role.ROLE_TEACHER));

        // ✅ pour les selects des modals create accounts
        model.addAttribute("allStudents", studentRepository.findAll());
        model.addAttribute("allTeachers", teacherRepository.findAll());

        return "admin/users/list";
    }

    // ➕ CREATE MANAGER
    @GetMapping("/managers/new")
    public String newManagerForm() {
        return "admin/users/new-manager";
    }

    @PostMapping("/managers")
    public String createManager(@RequestParam String username,
                                @RequestParam(required = false) String password,
                                RedirectAttributes ra) {
        try {
            var res = userAccountService.createManagerAccount(
                    username,
                    password,                 // ✅ peut être vide => généré
                    Set.of(Role.ROLE_MANAGER)
            );

            if (res.generatedPassword() != null) {
                ra.addFlashAttribute("generatedPassword",
                        "Password generated for " + res.account().getUsername() + ": " + res.generatedPassword());
            } else {
                ra.addFlashAttribute("success",
                        "Manager created: " + res.account().getUsername());
            }

            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/managers/new";
        }
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
                                       @RequestParam(required = false) String password,
                                       RedirectAttributes ra) {
        try {
            var res = userAccountService.createStudentAccount(username, password, studentId);

            if (res.generatedPassword() != null) {
                ra.addFlashAttribute("generatedPassword",
                        "Password generated for " + res.account().getUsername() + ": " + res.generatedPassword());
            } else {
                ra.addFlashAttribute("success",
                        "Student account created: " + res.account().getUsername());
            }

            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/students/new";
        }
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
                                       @RequestParam(required = false) String password,
                                       RedirectAttributes ra) {
        try {
            var res = userAccountService.createTeacherAccount(username, password, teacherId);

            if (res.generatedPassword() != null) {
                ra.addFlashAttribute("generatedPassword",
                        "Password generated for " + res.account().getUsername() + ": " + res.generatedPassword());
            } else {
                ra.addFlashAttribute("success",
                        "Teacher account created: " + res.account().getUsername());
            }

            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/teachers/new";
        }
    }

    // 🔒 ENABLE / DISABLE
    @PostMapping("/{username}/enable")
    public String toggleEnabled(@PathVariable String username,
                                @RequestParam boolean enabled,
                                RedirectAttributes ra) {
        try {
            userAccountService.toggleEnabled(username, enabled);
            ra.addFlashAttribute("success", "Updated enabled for: " + username);
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // 🔐 LOCK / UNLOCK
    @PostMapping("/{username}/lock")
    public String toggleLock(@PathVariable String username,
                             @RequestParam boolean locked,
                             RedirectAttributes ra) {
        try {
            userAccountService.toggleLock(username, locked);
            ra.addFlashAttribute("success", "Updated lock for: " + username);
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
