package com.eduflow.people.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    private String id;

    @Indexed(unique = true)
    private String matricule;     // ex: 25-ING-001

    private String firstName;
    private String lastName;

    @Indexed(unique = true, sparse = true)
    private String email;

    // pour savoir dans quelle classe il est cette année
    private String classGroupId;  // ref -> teaching.class_groups

    private Integer entryYear;    // année d’entrée
}
