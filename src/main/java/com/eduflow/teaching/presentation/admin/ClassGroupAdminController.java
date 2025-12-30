package com.eduflow.teaching.presentation.admin;

import com.eduflow.academic.repo.LevelRepository;
import com.eduflow.teaching.domain.ClassGroup;
import com.eduflow.teaching.service.ClassManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/class-groups")
@RequiredArgsConstructor
public class ClassGroupAdminController {

    private final ClassManagementService classGroupService;
    private final LevelRepository levelRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classGroups", classGroupService.getAllClassGroups());
        model.addAttribute("classGroup", new ClassGroup());
        model.addAttribute("levels", levelRepository.findAll());
        return "admin/teaching/class-groups/list";
    }

    @PostMapping
    public String create(@ModelAttribute ClassGroup classGroup) {
        classGroupService.createClassGroup(classGroup);
        return "redirect:/admin/class-groups";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable String id, @ModelAttribute ClassGroup classGroup) {
        classGroupService.updateClassGroup(id, classGroup);
        return "redirect:/admin/class-groups";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        classGroupService.deleteClassGroup(id);
        return "redirect:/admin/class-groups";
    }
}
