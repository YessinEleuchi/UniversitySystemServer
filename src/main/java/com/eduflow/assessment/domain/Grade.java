package com.eduflow.assessment.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("grades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndex(
        name = "student_evaluation_unique",
        def = "{'studentId':1,'evaluationId':1}",
        unique = true
)
public class Grade {

    @Id
    private String id;

    @Indexed
    private String evaluationId;   // ref -> assessments.evaluations

    @Indexed
    private String enrollmentId;   // lien direct vers Enrollment

    private Double score;          // ex: 14.5

    private String remark;         // optionnel: “absent”, “rendu en retard”…
}
