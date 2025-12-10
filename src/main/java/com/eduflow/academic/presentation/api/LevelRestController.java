package com.eduflow.academic.presentation.api;

import com.eduflow.academic.domain.Level;
import com.eduflow.academic.service.interfaces.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic")
@RequiredArgsConstructor
public class LevelRestController {

    private final LevelService levelService;

    @GetMapping("/specialities/{specialityId}/levels")
    public List<Level> getBySpeciality(@PathVariable String specialityId) {
        return levelService.getLevelsBySpeciality(specialityId);
    }

    @GetMapping("/levels/{id}")
    public Level getById(@PathVariable String id) {
        return levelService.getLevel(id);
    }


}
