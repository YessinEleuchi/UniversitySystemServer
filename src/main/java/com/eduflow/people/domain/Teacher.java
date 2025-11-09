package com.eduflow.people.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;      // ex: ENS-GL-01

    private String firstName;
    private String lastName;

    @Indexed(unique = true, sparse = true)
    private String email;
    private String phone;

    private String department; // Informatique, Réseaux, Génie électrique...
}
