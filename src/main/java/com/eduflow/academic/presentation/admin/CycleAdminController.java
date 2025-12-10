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
    public String listCycles(Model model) {
        model.addAttribute("cycles", cycleService.getAllCycles());
        return "admin/cycles/list"; // templates/admin/cycles/list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cycle", new Cycle());
        return "admin/cycles/form"; // form create/edit
    }

    @PostMapping
    public String create(@ModelAttribute Cycle cycle) {
        cycleService.createCycle(cycle);
        return "redirect:/admin/cycles";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("cycle", cycleService.getCycle(id));
        return "admin/cycles/form";
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
