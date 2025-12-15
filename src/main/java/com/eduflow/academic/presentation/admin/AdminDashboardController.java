package com.eduflow.academic.presentation.admin;

import com.eduflow.academic.repo.CycleRepository;
import com.eduflow.academic.repo.SpecialityRepository;
import com.eduflow.academic.repo.LevelRepository;
import com.eduflow.academic.repo.SemesterRepository;
import com.eduflow.academic.repo.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminDashboardController {

    private final CycleRepository cycleRepository;
    private final SpecialityRepository specialityRepository;
    private final LevelRepository levelRepository;
    private final SemesterRepository semesterRepository;
    private final SubjectRepository subjectRepository;

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("cyclesCount", cycleRepository.count());
        model.addAttribute("specialitiesCount", specialityRepository.count());
        model.addAttribute("levelsCount", levelRepository.count());
        model.addAttribute("semestersCount", semesterRepository.count());
        model.addAttribute("subjectsCount", subjectRepository.count());
        return "admin/dashboard";
    }
}
