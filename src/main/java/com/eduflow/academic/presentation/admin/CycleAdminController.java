package com.eduflow.academic.presentation.admin;

import com.eduflow.academic.domain.Cycle;
import com.eduflow.academic.service.interfaces.CycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/cycles")
@RequiredArgsConstructor
public class CycleAdminController {

    private final CycleService cycleService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("cycles", cycleService.getAllCycles());
        model.addAttribute("cycle", new Cycle()); // pour le form create
        return "admin/academic/cycles/list";
    }


    @PostMapping
    public String create(@ModelAttribute Cycle cycle) {
        cycleService.createCycle(cycle);
        return "redirect:/admin/cycles";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable String id, @ModelAttribute Cycle cycle) {
        cycleService.updateCycle(id, cycle);
        return "redirect:/admin/cycles";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        cycleService.deleteCycle(id);
        return "redirect:/admin/cycles";
    }
}
