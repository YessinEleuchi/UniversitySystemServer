package com.eduflow.academic.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("semesters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Semester {

    @Id
    private String id;

    @Indexed
    private String levelId;     // ref -> Level

    @Indexed
    private String code;        // S1, S2...

    private String label;       // Semestre 1, Semestre 2...
}
