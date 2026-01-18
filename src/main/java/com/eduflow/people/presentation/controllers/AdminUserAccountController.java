package com.eduflow.people.presentation.controllers;

import com.eduflow.people.domain.Role;
import com.eduflow.people.domain.Student;
import com.eduflow.people.domain.Teacher;
import com.eduflow.people.repo.StudentRepository;
import com.eduflow.people.repo.TeacherRepository;
import com.eduflow.people.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.eduflow.notification.AccountEmailDispatcher;
import com.eduflow.notification.AccountMailContext;
import com.eduflow.people.domain.PersonType;

import java.util.Set;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserAccountController {

    private final AccountEmailDispatcher accountEmailDispatcher;


    private final UserAccountService userAccountService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    // 📄 LIST USERS
    @GetMapping
    public String list(Model model) {
        model.addAttribute("managers", userAccountService.getAllByRole(Role.ROLE_MANAGER));
        model.addAttribute("students", userAccountService.getAllByRole(Role.ROLE_STUDENT));
        model.addAttribute("teachers", userAccountService.getAllByRole(Role.ROLE_TEACHER));

        model.addAttribute("allStudents", userAccountService.getStudentsWithoutAccount());
        model.addAttribute("allTeachers", userAccountService.getTeachersWithoutAccount());

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
            var res = userAccountService.createManagerAccount(username, password, Set.of(Role.ROLE_MANAGER));

            // ✅ envoyer mail seulement si un password a été généré
            if (res.generatedPassword() != null) {
                trySendMail(new AccountMailContext(
                        res.account().getUsername(),
                        res.account().getUsername(),
                        res.generatedPassword(),
                        null,
                        "Manager",
                        "http://localhost:8080/login"
                ), ra);

                ra.addFlashAttribute("generatedPassword",
                        "Password generated and emailed to " + res.account().getUsername());
            } else {
                ra.addFlashAttribute("success", "Manager created: " + res.account().getUsername());
            }

            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
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
                // récupère student pour displayName + email réel
                Student s = studentRepository.findById(studentId)
                        .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

                String to = (s.getEmail() != null && s.getEmail().contains("@"))
                        ? s.getEmail()
                        : res.account().getUsername(); // fallback

                trySendMail(new AccountMailContext(
                        to,
                        res.account().getUsername(),
                        res.generatedPassword(),
                        PersonType.STUDENT,
                        s.getLastName() + " " + s.getFirstName(),
                        "http://localhost:8080/login"
                ), ra);

                ra.addFlashAttribute("success",
                        "Student account created + password emailed to: " + to);
            } else {
                ra.addFlashAttribute("success", "Student account created: " + res.account().getUsername());
            }

            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
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
                Teacher t = teacherRepository.findById(teacherId)
                        .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + teacherId));

                String to = (t.getEmail() != null && t.getEmail().contains("@"))
                        ? t.getEmail()
                        : res.account().getUsername();

                trySendMail(new AccountMailContext(
                        to,
                        res.account().getUsername(),
                        res.generatedPassword(),
                        PersonType.TEACHER,
                        t.getLastName() + " " + t.getFirstName(),
                        "http://localhost:8080/login"
                ), ra);


                ra.addFlashAttribute("success",
                        "Teacher account created + password emailed to: " + to);
            } else {
                ra.addFlashAttribute("success", "Teacher account created: " + res.account().getUsername());
            }

            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
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
    private void trySendMail(AccountMailContext ctx, RedirectAttributes ra) {
        try {
            accountEmailDispatcher.sendAccountCreated(ctx);
        } catch (Exception mailEx) {
            // ✅ ne casse pas l'admin, on informe seulement
            ra.addFlashAttribute("warning",
                    "Account created, but email failed: " + mailEx.getMessage());
        }
    }

}
