package com.eduflow.people.service;

import com.eduflow.people.domain.UserAccount;
import com.eduflow.people.repo.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service de gestion des comptes utilisateurs (authentification Spring Security).
 * <p>
 * Un UserAccount est lié à une personne physique :
 * - personType: STUDENT | TEACHER | ADMIN | PARENT | STAFF
 * - personId: ID Mongo de l'étudiant/prof/...
 * <p>
 * Règles métier :
 * - Username unique (email ou matricule)
 * - Mot de passe hashé avec BCrypt
 * - Roles: ROLE_STUDENT, ROLE_TEACHER, ROLE_ADMIN...
 * - Compte activé par défaut, peut être verrouillé
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Set<String> VALID_ROLES = Set.of(
            "ROLE_STUDENT", "ROLE_TEACHER", "ROLE_ADMIN", "ROLE_PARENT", "ROLE_STAFF"
    );

    private static final Set<String> VALID_PERSON_TYPES = Set.of(
            "STUDENT", "TEACHER", "ADMIN", "PARENT", "STAFF"
    );

    /**
     * Crée un compte utilisateur (inscription ou admin).
     */
    public UserAccount createUser(String username,
                                  String rawPassword,
                                  Set<String> roles,
                                  String personId,
                                  String personType) {

        validateCreateUserParams(username, rawPassword, roles, personId, personType);

        if (userAccountRepository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalStateException("Nom d'utilisateur déjà utilisé : " + username);
        }

        UserAccount ua = UserAccount.builder()
                .username(username.toLowerCase().trim())
                .password(passwordEncoder.encode(rawPassword))
                .roles(roles.stream()
                        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r.toUpperCase())
                        .collect(Collectors.toSet()))
                .personId(personId)
                .personType(personType.toUpperCase())
                .enabled(true)
                .accountNonLocked(true)
                .createdAt(Instant.now())
                .build();

        return userAccountRepository.save(ua);
    }

    /**
     * Récupère un compte par username (pour Spring Security UserDetailsService).
     */
    public UserAccount getByUsername(String username) {
        return userAccountRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + username));
    }

    /**
     * Change le mot de passe (avec ancien mot de passe pour sécurité).
     */
    public void changePassword(String username, String oldPassword, String newPassword) {
        UserAccount ua = getByUsername(username);

        if (!passwordEncoder.matches(oldPassword, ua.getPassword())) {
            throw new IllegalStateException("Ancien mot de passe incorrect");
        }

        ua.setPassword(passwordEncoder.encode(newPassword));
        ua.setUpdatedAt(Instant.now());
        userAccountRepository.save(ua);
    }

    /**
     * Active/désactive un compte (admin).
     */
    public UserAccount toggleEnabled(String username, boolean enabled) {
        UserAccount ua = getByUsername(username);
        ua.setEnabled(enabled);
        ua.setUpdatedAt(Instant.now());
        return userAccountRepository.save(ua);
    }

    /**
     * Verrouille/déverrouille (après trop de tentatives).
     */
    public UserAccount toggleLock(String username, boolean locked) {
        UserAccount ua = getByUsername(username);
        ua.setAccountNonLocked(!locked);
        return userAccountRepository.save(ua);
    }

    /**
     * Trouve le compte lié à une personne (étudiant/prof).
     */
    public UserAccount getByPersonIdAndType(String personId, String personType) {
        return userAccountRepository.findByPersonIdAndPersonType(personId, personType.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aucun compte pour cette personne : " + personType + " " + personId));
    }

    /**
     * Tous les comptes d'un type (pour admin).
     */
    public java.util.List<UserAccount> getAllByPersonType(String personType) {
        return userAccountRepository.findByPersonTypeOrderByUsernameAsc(personType.toUpperCase());
    }

    // ========================================================================
    // VALIDATION INTERNE
    // ========================================================================
    private void validateCreateUserParams(String username, String rawPassword,
                                          Set<String> roles, String personId, String personType) {

        if (!StringUtils.hasText(username) || username.length() < 3) {
            throw new IllegalArgumentException("Username invalide (min 3 caractères)");
        }
        if (!username.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") &&
                !username.matches("^[a-zA-Z0-9]{3,20}$")) {
            throw new IllegalArgumentException("Username doit être un email ou alphanumérique");
        }
        if (rawPassword == null || rawPassword.length() < 6) {
            throw new IllegalArgumentException("Mot de passe trop court (min 6)");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Au moins un rôle requis");
        }
        if (!VALID_ROLES.containsAll(roles)) {
            throw new IllegalArgumentException("Rôle invalide. Autorisé : " + VALID_ROLES);
        }
        if (!StringUtils.hasText(personId)) {
            throw new IllegalArgumentException("personId obligatoire");
        }
        if (!VALID_PERSON_TYPES.contains(personType.toUpperCase())) {
            throw new IllegalArgumentException("personType invalide : " + personType);
        }
    }
}