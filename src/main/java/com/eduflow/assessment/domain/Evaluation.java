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
    private String courseInstanceId;

    private String title;

    private AssessmentType type;

    private Double maxScore;

    private Double weight;

    private Instant evaluationDate;
}
