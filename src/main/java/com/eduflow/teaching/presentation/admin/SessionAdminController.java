package com.eduflow.teaching.presentation.admin;

import com.eduflow.academic.repo.SubjectRepository;
import com.eduflow.people.repo.TeacherRepository;
import com.eduflow.teaching.repo.ClassGroupRepository;
import com.eduflow.teaching.repo.CourseInstanceRepository;
import com.eduflow.teaching.repo.RoomRepository;
import com.eduflow.teaching.repo.SessionRepository;
import com.eduflow.teaching.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/sessions")
@RequiredArgsConstructor
public class SessionAdminController {

    private final SessionRepository sessionRepository;
    private final SessionService sessionService;

    private final CourseInstanceRepository courseInstanceRepository;
    private final RoomRepository roomRepository;

    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ClassGroupRepository classGroupRepository;

    private static final ZoneId ZONE = ZoneId.of("Africa/Tunis");

    @GetMapping
    public String list(Model model) {

        var sessions = sessionRepository.findAll();
        var courseInstances = courseInstanceRepository.findAll();
        var rooms = roomRepository.findAll();

        var subjects = subjectRepository.findAll();
        var teachers = teacherRepository.findAll();
        var classGroups = classGroupRepository.findAll();

        // labels for UI
        var subjectLabel = subjects.stream().collect(Collectors.toMap(
                s -> s.getId(),
                s -> s.getCode() + " - " + s.getTitle()
        ));

        var teacherLabel = teachers.stream().collect(Collectors.toMap(
                t -> t.getId(),
                t -> t.getCode() + " - " + t.getLastName() + " " + t.getFirstName()
        ));

        var classGroupLabel = classGroups.stream().collect(Collectors.toMap(
                cg -> cg.getId(),
                cg -> cg.getCode() + (cg.getLabel() != null ? " - " + cg.getLabel() : "")
        ));

        var roomLabel = rooms.stream().collect(Collectors.toMap(
                r -> r.getId(),
                r -> r.getCode() + (r.getBuilding() != null ? " (" + r.getBuilding() + ")" : "")
        ));

        // CourseInstance label = Subject + ClassGroup + Year + Semester
        var courseInstanceLabel = courseInstances.stream().collect(Collectors.toMap(
                ci -> ci.getId(),
                ci -> {
                    String subj = subjectLabel.getOrDefault(ci.getSubjectId(), ci.getSubjectId());
                    String cg = classGroupLabel.getOrDefault(ci.getClassGroupId(), ci.getClassGroupId());
                    return subj + " | " + cg + " | " + ci.getSemesterCode() + " | " + ci.getAcademicYear();
                }
        ));

        model.addAttribute("sessions", sessions);
        model.addAttribute("courseInstances", courseInstances);
        model.addAttribute("rooms", rooms);

        model.addAttribute("subjectLabel", subjectLabel);
        model.addAttribute("teacherLabel", teacherLabel);
        model.addAttribute("classGroupLabel", classGroupLabel);
        model.addAttribute("roomLabel", roomLabel);
        model.addAttribute("courseInstanceLabel", courseInstanceLabel);

        return "admin/teaching/sessions/list";
    }

    @PostMapping
    public String create(@RequestParam String courseInstanceId,
                         @RequestParam String roomId,
                         @RequestParam String type,
                         @RequestParam String startLocal,
                         @RequestParam String endLocal,
                         RedirectAttributes ra) {
        try {
            Instant start = toInstant(startLocal);
            Instant end = toInstant(endLocal);

            sessionService.createSession(courseInstanceId, start, end, roomId, type);

            ra.addFlashAttribute("success", "Séance créée avec succès ✅");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            ra.addFlashAttribute("error", ex.getMessage());

            // conserver valeurs saisies
            ra.addFlashAttribute("form_courseInstanceId", courseInstanceId);
            ra.addFlashAttribute("form_roomId", roomId);
            ra.addFlashAttribute("form_type", type);
            ra.addFlashAttribute("form_startLocal", startLocal);
            ra.addFlashAttribute("form_endLocal", endLocal);
        }
        return "redirect:/admin/sessions";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable String id,
                         @RequestParam String roomId,
                         @RequestParam String type,
                         @RequestParam String startLocal,
                         @RequestParam String endLocal,
                         RedirectAttributes ra) {
        try {
            Instant start = toInstant(startLocal);
            Instant end = toInstant(endLocal);

            sessionService.updateSession(id, start, end, roomId, type);

            ra.addFlashAttribute("success", "Séance mise à jour ✅");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/sessions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try {
            sessionService.deleteSession(id);
            ra.addFlashAttribute("success", "Séance supprimée ✅");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/sessions";
    }

    private static Instant toInstant(String localDateTime) {
        LocalDateTime ldt = LocalDateTime.parse(localDateTime);
        return ldt.atZone(ZONE).toInstant();
    }
}
