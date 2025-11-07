package com.eduflow.academic.domain;

import com.eduflow.academic.domain.enums.ProgramCycle;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("cycles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cycle {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private ProgramCycle label;
}
