package com.eduflow.academic.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("courses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Course {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;      // CS101

    private String title;     // Programmation 1

    @Indexed
    private String subjectId; // Informatique
}
