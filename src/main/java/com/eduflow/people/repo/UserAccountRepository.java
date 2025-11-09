package com.eduflow.people.repo;

import com.eduflow.people.domain.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    boolean existsByUsernameIgnoreCase(String username);

    Optional<UserAccount> findByUsernameIgnoreCase(String username);

    Optional<UserAccount> findByPersonIdAndPersonType(String personId, String personType);

    List<UserAccount> findByPersonTypeOrderByUsernameAsc(String personType);

    // Pour Spring Security
    Optional<UserAccount> findByUsername(String username);
}
