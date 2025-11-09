package com.eduflow.assessment.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

    @Id
    private String id;

    @Indexed
    private String courseInstanceId;   // ref -> teaching.course_instances

    private String title;              // DS1, TP Réseau, Examen final...

    private AssessmentType type;       // DS, TP, EXAM...

    private Double maxScore;           // ex: 20.0

    private Double weight;             // ex: 0.3 pour 30% de la note finale

    private Instant evaluationDate;    // quand l’éval a lieu
}
