package com.eduflow.academic.presentation.api;

import com.eduflow.academic.domain.Speciality;
import com.eduflow.academic.service.interfaces.SpecialityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic")
@RequiredArgsConstructor
public class SpecialityRestController {

    private final SpecialityService specialityService;

    @GetMapping("/cycles/{cycleId}/specialities")
    public List<Speciality> getByCycle(@PathVariable String cycleId) {
        return specialityService.getSpecialitiesByCycle(cycleId);
    }

    @GetMapping("/specialities/{id}")
    public Speciality getById(@PathVariable String id) {
        return specialityService.getSpeciality(id);
    }
}
