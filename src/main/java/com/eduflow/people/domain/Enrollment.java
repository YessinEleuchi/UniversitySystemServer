package com.eduflow.people.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("enrollments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CompoundIndex(name="uq_student_year", def="{'studentId':1,'academicYear':1}", unique=true)
public class Enrollment {

    @Id
    private String id;

    private String studentId;

    private Integer academicYear;     // ex: 2025

    private String levelId;           // ref academic.level
    private String classGroupId;      // ref teaching.class_groups

    private EnrollmentStatus status;  // ENROLLED, CLOSED
    private Decision decision;

    private Double average;
    private Integer earnedCredits;

    private Instant createdAt;
    private Instant closedAt;
}
