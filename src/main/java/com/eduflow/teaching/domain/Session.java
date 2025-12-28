package com.eduflow.teaching.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("sessions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Session {
    @Id
    private String id;

    @Indexed
    private String courseInstanceId;

    @Indexed
    private String teacherId;

    @Indexed
    private String classGroupId;

    @Indexed
    private String roomId;

    private Instant startTime;
    private Instant endTime;

    private String type;
}
