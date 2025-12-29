package com.eduflow.teaching.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("course_instances")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CompoundIndex(
        name = "uq_course_instance",
        def = "{'classGroupId': 1, 'subjectId': 1, 'semesterCode': 1, 'academicYear': 1}",
        unique = true
)
public class CourseInstance {

    @Id
    private String id;

    @Indexed
    private String subjectId;     // ref -> subjects

    @Indexed
    private String classGroupId;  // ref -> class_groups

    @Indexed
    private String teacherId;     // ref -> teachers

    @Indexed
    private String semesterCode;  // "S1", "S2" (global)

    private Integer academicYear; // 2024
}
