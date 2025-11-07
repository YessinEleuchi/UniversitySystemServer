package com.eduflow.academic.repo;

import com.eduflow.academic.domain.Level;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LevelRepository extends MongoRepository<Level, String> {

    // tous les niveaux d’une spécialité
    List<Level> findBySpecialityId(String specialityId);

    // ex: ING2
    Optional<Level> findByCodeAndSpecialityId(String code, String specialityId);
}
