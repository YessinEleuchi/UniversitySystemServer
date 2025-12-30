package com.eduflow.teaching.presentation.admin;

import com.eduflow.academic.repo.SubjectRepository;
import com.eduflow.people.repo.TeacherRepository;
import com.eduflow.teaching.repo.ClassGroupRepository;
import com.eduflow.teaching.repo.CourseInstanceRepository;
import com.eduflow.teaching.service.CourseInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/teaching/course-instances")
@RequiredArgsConstructor
public class CourseInstanceAdminController {

    private final CourseInstanceRepository courseInstanceRepository;
    private final CourseInstanceService courseInstanceService;

    private final SubjectRepository subjectRepository;
    private final ClassGroupRepository classGroupRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping
    public String list(Model model) {
        var subjects = subjectRepository.findAll();
        var classGroups = classGroupRepository.findAll();
        var teachers = teacherRepository.findAll();

        // Maps id -> label (pour affichage)
        var subjectLabel = subjects.stream().collect(Collectors.toMap(
                s -> s.getId(),
                s -> (s.getCode() + " - " + s.getTitle())
        ));

        var classGroupLabel = classGroups.stream().collect(Collectors.toMap(
                cg -> cg.getId(),
                cg -> (cg.getCode() + (cg.getLabel() != null ? " - " + cg.getLabel() : ""))
        ));

        var teacherLabel = teachers.stream().collect(Collectors.toMap(
                t -> t.getId(),
                t -> (t.getCode() + " - " + t.getLastName() + " " + t.getFirstName())
        ));

        model.addAttribute("courseInstances", courseInstanceRepository.findAll());
        model.addAttribute("subjects", subjects);
        model.addAttribute("classGroups", classGroups);
        model.addAttribute("teachers", teachers);

        model.addAttribute("subjectLabel", subjectLabel);
        model.addAttribute("classGroupLabel", classGroupLabel);
        model.addAttribute("teacherLabel", teacherLabel);

        return "admin/teaching/course-instances/list";
    }


    @PostMapping
    public String openCourse(@RequestParam String subjectId,
                             @RequestParam String classGroupId,
                             @RequestParam String teacherId,
                             @RequestParam(required = false) Integer academicYear,
                             RedirectAttributes ra) {
        try {
            courseInstanceService.openCourse(subjectId, classGroupId, teacherId, null, academicYear);
            ra.addFlashAttribute("success", "Cours ouvert avec succès ✅");
        } catch (IllegalStateException | IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            // (optionnel) garder les valeurs sélectionnées pour pré-remplir
            ra.addFlashAttribute("form_subjectId", subjectId);
            ra.addFlashAttribute("form_classGroupId", classGroupId);
            ra.addFlashAttribute("form_teacherId", teacherId);
            ra.addFlashAttribute("form_academicYear", academicYear);
        }
        return "redirect:/admin/teaching/course-instances";
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        courseInstanceRepository.deleteById(id);
        return "redirect:/admin/teaching/course-instances";
    }
}
