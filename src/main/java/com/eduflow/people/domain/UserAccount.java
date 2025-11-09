package com.eduflow.people.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;      // à encoder côté service

    private Set<String> roles;    // ["ADMIN", "STUDENT", "TEACHER"]

    // lien vers student ou teacher si besoin
    private String personId;
    private String personType;    // "STUDENT" ou "TEACHER"
}
