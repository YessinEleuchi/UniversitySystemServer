package com.eduflow.people.service.dto;

import com.eduflow.people.domain.Decision;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PromotionLine {
    private String studentId;
    private String fullName;
    private Decision decision;

    private String fromClassGroupId;
    private String fromLevelId;

    private String action;          // PROMOTE / REPEAT / DROP / GRADUATE / HOLD
    private String suggestedToClassGroupId; // pré-rempli si tu veux
}
