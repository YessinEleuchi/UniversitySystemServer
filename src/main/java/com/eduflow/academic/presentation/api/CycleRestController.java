package com.eduflow.academic.presentation.api;

import com.eduflow.academic.domain.Cycle;
import com.eduflow.academic.service.interfaces.CycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cycles")
@RequiredArgsConstructor
public class CycleRestController {

    private final CycleService cycleService;

    @GetMapping
    public List<Cycle> getAll() {
        return cycleService.getAllCycles();
    }

    @GetMapping("/{id}")
    public Cycle getOne(@PathVariable String id) {
        return cycleService.getCycle(id);
    }
}