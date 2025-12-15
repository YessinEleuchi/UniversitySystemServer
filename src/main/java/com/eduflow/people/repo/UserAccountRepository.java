package com.eduflow.people.repo;

import com.eduflow.people.domain.PersonType;
import com.eduflow.people.domain.Role;
import com.eduflow.people.domain.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    // Username / login
    boolean existsByUsernameIgnoreCase(String username);
    Optional<UserAccount> findByUsernameIgnoreCase(String username);

    // Pour Spring Security (si tu utilises username normalisé en lower-case, tu peux garder seulement IgnoreCase)
    Optional<UserAccount> findByUsername(String username);

    // Linking to people (Teacher/Student)
    Optional<UserAccount> findByPersonIdAndPersonType(String personId, PersonType personType);

    List<UserAccount> findByPersonTypeOrderByUsernameAsc(PersonType personType);

    // Role-based queries (useful for listing admins/managers)
    List<UserAccount> findByRolesContainingOrderByUsernameAsc(Role role);

    long countByRolesContaining(Role role);
}
