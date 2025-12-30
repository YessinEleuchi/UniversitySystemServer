package com.eduflow.people.service;

import com.eduflow.people.domain.Decision;
import com.eduflow.people.domain.Enrollment;
import com.eduflow.people.domain.EnrollmentStatus;
import com.eduflow.people.repo.EnrollmentRepository;
import com.eduflow.people.repo.StudentRepository;
import com.eduflow.people.service.dto.PromotionLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public List<PromotionLine> preview(Integer fromYear,
                                       Integer toYear,
                                       String fromLevelId,
                                       String toLevelId) {

        List<Enrollment> current = enrollmentRepository
                .findByAcademicYearAndLevelIdAndStatus(fromYear, fromLevelId, EnrollmentStatus.ENROLLED);

        if (current.isEmpty()) return List.of();

        // charger noms étudiants en 1 shot
        var students = studentRepository.findAllById(current.stream().map(Enrollment::getStudentId).toList());
        Map<String, String> nameById = new HashMap<>();
        for (var s : students) {
            // adapte selon ton Student (firstName/lastName ou fullName)
            String fullName = (s.getFirstName() + " " + s.getLastName()).trim();
            nameById.put(s.getId(), fullName.isBlank() ? s.getId() : fullName);
        }

        List<PromotionLine> lines = new ArrayList<>();
        for (Enrollment e : current) {
            Decision d = e.getDecision() == null ? Decision.NONE : e.getDecision();

            String action;
            if (d == Decision.PASS) action = "PROMOTE";
            else if (d == Decision.FAIL) action = "REPEAT";
            else if (d == Decision.DROPPED) action = "DROP";
            else if (d == Decision.GRADUATED) action = "GRADUATE";
            else action = "HOLD";

            lines.add(new PromotionLine(
                    e.getStudentId(),
                    nameById.getOrDefault(e.getStudentId(), e.getStudentId()),
                    d,
                    e.getClassGroupId(),
                    e.getLevelId(),
                    action,
                    null
            ));
        }
        return lines;
    }

    public void commit(Integer fromYear,
                       Integer toYear,
                       String fromLevelId,
                       String toLevelId,
                       List<String> studentIds,
                       List<String> toClassGroupIds,
                       List<String> actions) {

        if (studentIds == null || studentIds.isEmpty()) return;
        if (toClassGroupIds == null || toClassGroupIds.size() != studentIds.size())
            throw new IllegalArgumentException("Mapping studentId -> toClassGroupId invalide");
        if (actions == null || actions.size() != studentIds.size())
            throw new IllegalArgumentException("Mapping studentId -> action invalide");

        for (int i = 0; i < studentIds.size(); i++) {
            String studentId = studentIds.get(i);
            String toClassGroupId = toClassGroupIds.get(i);
            String action = actions.get(i);

            Enrollment current = enrollmentRepository.findByStudentIdAndAcademicYear(studentId, fromYear)
                    .orElseThrow(() -> new IllegalStateException("Enrollment manquant (fromYear) pour: " + studentId));

            // Ne pas re-committer
            if (enrollmentRepository.existsByStudentIdAndAcademicYear(studentId, toYear)) {
                // déjà inscrit pour l’année cible => on ignore ou on throw
                continue;
            }

            // On clôture l’enrollment actuel
            current.setStatus(EnrollmentStatus.CLOSED);
            current.setClosedAt(Instant.now());
            enrollmentRepository.save(current);

            // Selon action, on crée ou pas le nouveau enrollment
            if ("DROP".equals(action) || "GRADUATE".equals(action)) {
                // pas d’inscription l’année suivante
                continue;
            }
            if ("HOLD".equals(action)) {
                // sécurité : on n’inscrit pas sans décision
                continue;
            }

            // REPEAT => même levelId, PROMOTE => toLevelId
            String targetLevelId = "REPEAT".equals(action) ? fromLevelId : toLevelId;

            Enrollment next = Enrollment.builder()
                    .studentId(studentId)
                    .academicYear(toYear)
                    .levelId(targetLevelId)
                    .classGroupId(toClassGroupId)
                    .status(EnrollmentStatus.ENROLLED)
                    .decision(Decision.NONE)
                    .createdAt(Instant.now())
                    .build();

            enrollmentRepository.save(next);
        }
    }
}
