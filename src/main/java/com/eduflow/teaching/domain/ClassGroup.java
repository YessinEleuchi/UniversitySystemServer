package com.eduflow.teaching.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("class_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassGroup {

    @Id
    private String id;

    @Indexed
    private String levelId;    // ref -> academic.level

    @Indexed
    private Integer academicYear;   // 2025, 2026...

    @Indexed(unique = true)
    private String code;       // ING2-GL-2025-A

    private String label;      // Classe ING2 GL groupe A
}
