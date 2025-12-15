package com.eduflow.academic.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;         // WEB201, ALG303...

    private String title;        // Développement Web Avancé

    @Indexed
    private String semesterId;   // ref -> Semester

    private Integer credits;     // optionnel: 3, 6...

    private Double coefficient; // si vous utilisez coef
}
