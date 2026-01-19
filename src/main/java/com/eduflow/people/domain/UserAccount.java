package com.eduflow.people.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

@Document("users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserAccount {

    @Id private String id;

    @Indexed(unique = true)
    private String username; // email/login

    private String password; // BCrypt

    @Builder.Default
    private Set<Role> roles = Set.of();


    private String personId;
    private PersonType personType;

    @Builder.Default private boolean enabled = true;
    @Builder.Default private boolean accountNonLocked = true;

    private Instant createdAt;
    private Instant updatedAt;
}


