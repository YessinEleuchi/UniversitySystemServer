package com.eduflow.teaching.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("rooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;        // B12, A101, LAB-3...

    private String building;    // Bloc A, Bloc B...
    private Integer floor;      // 0, 1, 2...
    private Integer capacity;   // 30, 60...

    private RoomType type;      // CLASSROOM, LAB, AMPHI
    private boolean active;     // true/false
}

