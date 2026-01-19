package com.eduflow.security.presentation.restController.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private Set<String> roles;
    private String personType; // STUDENT/TEACHER/null
    private String personId;   // Student.id ou Teacher.id
    private String username;
}
