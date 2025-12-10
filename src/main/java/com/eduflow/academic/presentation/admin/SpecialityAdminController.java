package com.eduflow.academic.presentation.admin;

import com.eduflow.academic.domain.Speciality;
import com.eduflow.academic.service.interfaces.CycleService;
import com.eduflow.academic.service.interfaces.SpecialityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/academic")
@RequiredArgsConstructor
public class SpecialityAdminController {

    private final SpecialityService specialityService;
    private final CycleService cycleService;

    // Liste des spécialités d’un cycle
    @GetMapping("/cycles/{cycleId}/specialities")
    public String listByCycle(@PathVariable String cycleId, Model model) {
        model.addAttribute("cycle", cycleService.getCycle(cycleId));
        model.addAttribute("specialities", specialityService.getSpecialitiesByCycle(cycleId));
        return "admin/academic/specialities/list";
    }

    @GetMapping("/cycles/{cycleId}/specialities/new")
    public String showCreateForm(@PathVariable String cycleId, Model model) {
        Speciality speciality = new Speciality();
        speciality.setCycleId(cycleId);
        model.addAttribute("cycle", cycleService.getCycle(cycleId));
        model.addAttribute("speciality", speciality);
        return "admin/academic/specialities/form";
    }

    @PostMapping("/cycles/{cycleId}/specialities")
    public String create(@PathVariable String cycleId,
                         @ModelAttribute Speciality speciality) {
        specialityService.createSpeciality(cycleId, speciality);
        return "redirect:/admin/academic/cycles/" + cycleId + "/specialities";
    }

    @GetMapping("/specialities/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Speciality speciality = specialityService.getSpeciality(id);
        model.addAttribute("speciality", speciality);
        model.addAttribute("cycle", cycleService.getCycle(speciality.getCycleId()));
        return "admin/academic/specialities/form";
    }

    @PostMapping("/specialities/{id}")
    public String update(@PathVariable String id,
                         @ModelAttribute Speciality speciality) {
        Speciality updated = specialityService.updateSpeciality(id, speciality);
        return "redirect:/admin/academic/cycles/" + updated.getCycleId() + "/specialities";
    }

    @PostMapping("/specialities/{id}/delete")
    public String delete(@PathVariable String id) {
        Speciality speciality = specialityService.getSpeciality(id);
        String cycleId = speciality.getCycleId();
        specialityService.deleteSpeciality(id);
        return "redirect:/admin/academic/cycles/" + cycleId + "/specialities";
    }
}
