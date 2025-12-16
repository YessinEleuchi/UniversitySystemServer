package com.eduflow.people.repo;

import com.eduflow.people.domain.PersonType;
import com.eduflow.people.domain.Role;
import com.eduflow.people.domain.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {
    Optional<UserAccount> findByUsername(String username);
    List<UserAccount> findByPersonTypeOrderByUsernameAsc(PersonType personType);
    List<UserAccount> findByRolesContainingOrderByUsernameAsc(Role role);
    long countByRolesContaining(Role role);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByPersonIdAndPersonType(String personId, PersonType personType);
    Optional<UserAccount> findByUsernameIgnoreCase(String username);
    Optional<UserAccount> findByPersonIdAndPersonType(String personId, PersonType personType);
}
