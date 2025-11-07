package com.eduflow.teaching.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndex(
        name = "student_course_unique",
        def = "{'studentId':1,'courseInstanceId':1}",
        unique = true
)
public class Enrollment {

    @Id
    private String id;

    @Indexed
    private String studentId;        // ref -> people.student

    @Indexed
    private String courseInstanceId; // ref -> teaching.courseInstance

    private Instant enrolledAt;
}
