package com.eduflow.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    private String secret;
    private String issuer = "eduflow";
    private int accessMinutes = 15;
}
