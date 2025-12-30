package com.eduflow.people.presentation;

import com.eduflow.academic.repo.LevelRepository;
import com.eduflow.teaching.repo.ClassGroupRepository;
import com.eduflow.people.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/promotions")
@RequiredArgsConstructor
public class PromotionAdminController {

    private final PromotionService promotionService;
    private final LevelRepository levelRepository;
    private final ClassGroupRepository classGroupRepository;

    @GetMapping
    public String wizard(@RequestParam(required = false) Integer fromYear,
                         @RequestParam(required = false) Integer toYear,
                         @RequestParam(required = false) String fromLevelId,
                         @RequestParam(required = false) String toLevelId,
                         Model model) {

        model.addAttribute("levels", levelRepository.findAll());
        model.addAttribute("classGroups", classGroupRepository.findAll());

        model.addAttribute("fromYear", fromYear);
        model.addAttribute("toYear", toYear);
        model.addAttribute("fromLevelId", fromLevelId);
        model.addAttribute("toLevelId", toLevelId);

        if (fromYear != null && toYear != null && fromLevelId != null && toLevelId != null) {
            model.addAttribute("preview", promotionService.preview(fromYear, toYear, fromLevelId, toLevelId));
        }

        return "admin/users/promotions/wizard";
    }

    @PostMapping("/commit")
    public String commit(@RequestParam Integer fromYear,
                         @RequestParam Integer toYear,
                         @RequestParam String fromLevelId,
                         @RequestParam String toLevelId,
                         @RequestParam(name = "studentId") java.util.List<String> studentIds,
                         @RequestParam(name = "toClassGroupId") java.util.List<String> toClassGroupIds,
                         @RequestParam(name = "action") java.util.List<String> actions) {

        promotionService.commit(fromYear, toYear, fromLevelId, toLevelId, studentIds, toClassGroupIds, actions);

        return "redirect:/admin/promotions?fromYear=" + fromYear +
                "&toYear=" + toYear +
                "&fromLevelId=" + fromLevelId +
                "&toLevelId=" + toLevelId;
    }
}
