package com.eduflow.academic.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("levels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Level {

    @Id
    private String id;

    @Indexed
    private String specialityId;   // ref -> Speciality

    @Indexed
    private String code;           // ING1, ING2, L3...

    private String label;          // 1ère année ingénieur GL...
}
