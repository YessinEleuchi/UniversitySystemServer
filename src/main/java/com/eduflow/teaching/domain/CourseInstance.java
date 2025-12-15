package com.eduflow.teaching.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("course_instances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseInstance {

    @Id
    private String id;

    @Indexed
    private String courseId;     // ref -> courses

    @Indexed
    private String classGroupId; // L2 GL A

    @Indexed
    private String teacherId;    // Dr Ahmed

    @Indexed
    private String semesterId;   // S1

    private Integer academicYear; // 2024
}
