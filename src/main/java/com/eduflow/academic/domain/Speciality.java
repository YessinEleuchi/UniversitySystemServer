package com.eduflow.academic.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("specialities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Speciality {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;      // GL, SI, IA...

    private String label;     // Génie Logiciel, Systèmes d'info...

    @Indexed
    private String cycleId;   // ref -> Cycle
}
