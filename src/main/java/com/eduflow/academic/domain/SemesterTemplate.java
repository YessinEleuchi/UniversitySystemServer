package com.eduflow.academic.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("semester_templates")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SemesterTemplate {
    @Id private String id;
    @Indexed(unique = true)
    private String title;
}
