package com.eduflow.teaching.repo;


import com.eduflow.teaching.domain.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends MongoRepository<Room, String> {
    boolean existsByCodeIgnoreCase(String code);
    Optional<Room> findByCodeIgnoreCase(String code);
    List<Room> findByActiveTrueOrderByCodeAsc();
}

