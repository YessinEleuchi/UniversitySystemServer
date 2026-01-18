package com.eduflow.academic.presentation.controllers;

import com.eduflow.academic.domain.Level;
import com.eduflow.academic.service.interfaces.LevelService;
import com.eduflow.academic.service.interfaces.SpecialityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/academic")
@RequiredArgsConstructor
public class LevelAdminController {

    private final LevelService levelService;
    private final SpecialityService specialityService;

    @GetMapping("/specialities/{specialityId}/levels")
    public String listBySpeciality(@PathVariable String specialityId, Model model) {
        model.addAttribute("speciality", specialityService.getSpeciality(specialityId));
        model.addAttribute("levels", levelService.getLevelsBySpeciality(specialityId));
        return "admin/academic/levels/list";
    }

    @PostMapping("/specialities/{specialityId}/levels")
    public String create(@PathVariable String specialityId,
                         @ModelAttribute("level") Level level) {
        level.setSpecialityId(specialityId);
        levelService.createLevel(specialityId, level);
        return "redirect:/admin/academic/specialities/" + specialityId + "/levels";
    }

    @PostMapping("/levels/{id}")
    public String update(@PathVariable String id,
                         @ModelAttribute("level") Level level) {
        Level updated = levelService.updateLevel(id, level);
        return "redirect:/admin/academic/specialities/" + updated.getSpecialityId() + "/levels";
    }

    @PostMapping("/levels/{id}/delete")
    public String delete(@PathVariable String id) {
        Level level = levelService.getLevel(id);
        String specialityId = level.getSpecialityId();
        levelService.deleteLevel(id);
        return "redirect:/admin/academic/specialities/" + specialityId + "/levels";
    }
}
