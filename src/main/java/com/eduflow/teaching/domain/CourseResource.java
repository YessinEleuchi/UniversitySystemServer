package com.eduflow.teaching.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("course_resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResource {

    @Id
    private String id;

    @Indexed
    private String courseId;          // obligatoire

    @Indexed
    private String courseInstanceId;  // optionnel (null = global)

    @Indexed
    private String teacherId;

    private String title;

    private String fileId;

    private Integer academicYear;

    private Boolean published;        // visible aux étudiants
}
