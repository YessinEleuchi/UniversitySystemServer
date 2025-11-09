package com.eduflow.people.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

@Document("users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

    @Id private String id;
    private String username; // email ou login
    private String password;
    private Set<String> roles; // ROLE_TEACHER, ROLE_STUDENT...
    private String personId;
    private String personType; // TEACHER, STUDENT, ADMIN...

    private boolean enabled = true;
    private boolean accountNonLocked = true;
    private Instant createdAt;
    private Instant updatedAt;
}
