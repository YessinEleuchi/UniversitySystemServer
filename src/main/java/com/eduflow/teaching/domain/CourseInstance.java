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
    private String subjectId;      // ref -> academic.subject

    @Indexed
    private String classGroupId;   // ref -> teaching.classGroup

    @Indexed
    private String teacherId;      // ref -> people.teacher

    @Indexed
    private String semesterId;     // ref -> academic.semester

    private Integer academicYear;  // pour filtrer par année
}
