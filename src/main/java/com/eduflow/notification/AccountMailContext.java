package com.eduflow.notification;

import com.eduflow.people.domain.PersonType;

public record AccountMailContext(
        String toEmail,
        String username,
        String generatedPassword,
        PersonType personType,
        String displayName,
        String loginUrl
) {}
