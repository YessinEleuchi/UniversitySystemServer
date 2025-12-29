package com.eduflow.academic.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("semesters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Semester {

    @Id
    private String id;         // généré par Mongo

    private String code;       // "S1", "S2" → unique
    private String label;      // "Semestre 1"
    private int order;         // 1, 2, ...
    private boolean active = true;
}