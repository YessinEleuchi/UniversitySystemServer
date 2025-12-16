package com.eduflow.people.service;

import com.eduflow.people.domain.PersonType;
import com.eduflow.people.domain.Role;
import com.eduflow.people.domain.Student;
import com.eduflow.people.domain.Teacher;
import com.eduflow.people.domain.UserAccount;
import com.eduflow.people.repo.StudentRepository;
import com.eduflow.people.repo.TeacherRepository;
import com.eduflow.people.repo.UserAccountRepository;
import com.eduflow.security.PasswordGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountService {

    private final UserAccountRepository userRepo;
    private final StudentRepository studentRepo;
    private final TeacherRepository teacherRepo;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGeneratorService passwordGeneratorService;
    public record AccountCreationResult(UserAccount account, String generatedPassword) {}


    // -------------------------
    // CREATE ACCOUNTS
    // -------------------------

    public AccountCreationResult createManagerAccount(String username, String rawPassword, Set<Role> roles) {
        if (roles == null || roles.isEmpty())
            throw new IllegalArgumentException("At least one role is required");
        if (!(roles.contains(Role.ROLE_MANAGER) || roles.contains(Role.ROLE_SUPER_ADMIN)))
            throw new IllegalArgumentException("Manager account must have ROLE_MANAGER or ROLE_SUPER_ADMIN");

        return createBase(username, rawPassword, roles, null, null);
    }

    public AccountCreationResult createStudentAccount(String username, String rawPassword, String studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        return createBase(username, rawPassword, Set.of(Role.ROLE_STUDENT), student.getId(), PersonType.STUDENT);
    }

    public AccountCreationResult createTeacherAccount(String username, String rawPassword, String teacherId) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + teacherId));

        return createBase(username, rawPassword, Set.of(Role.ROLE_TEACHER), teacher.getId(), PersonType.TEACHER);
    }

    private AccountCreationResult createBase(String username,
                                             String rawPassword,
                                             Set<Role> roles,
                                             String personId,
                                             PersonType personType) {

        String u = normalizeUsername(username);

        if (userRepo.existsByUsernameIgnoreCase(u)) {
            throw new IllegalStateException("Username already used: " + u);
        }

        validateRolePersonLink(roles, personId, personType);

        // ✅ generate if empty
        String generated = null;
        String finalRawPassword = rawPassword;

        if (!StringUtils.hasText(finalRawPassword)) {
            generated = passwordGeneratorService.generate(12);
            finalRawPassword = generated;
        }

        validatePassword(finalRawPassword);

        Instant now = Instant.now();
        UserAccount ua = UserAccount.builder()
                .username(u)
                .password(passwordEncoder.encode(finalRawPassword))
                .roles(roles)
                .personId(personId)
                .personType(personType)
                .enabled(true)
                .accountNonLocked(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserAccount saved = userRepo.save(ua);
        return new AccountCreationResult(saved, generated);
    }

    // -------------------------
    // READ / UPDATE
    // -------------------------

    public UserAccount getByUsername(String username) {
        String u = normalizeUsername(username);
        return userRepo.findByUsernameIgnoreCase(u)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + u));
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        UserAccount ua = getByUsername(username);

        if (!passwordEncoder.matches(oldPassword, ua.getPassword())) {
            throw new IllegalStateException("Old password incorrect");
        }

        validatePassword(newPassword);

        ua.setPassword(passwordEncoder.encode(newPassword));
        ua.setUpdatedAt(Instant.now());
        userRepo.save(ua);
    }

    public UserAccount toggleEnabled(String username, boolean enabled) {
        UserAccount ua = getByUsername(username);
        ua.setEnabled(enabled);
        ua.setUpdatedAt(Instant.now());
        return userRepo.save(ua);
    }

    public UserAccount toggleLock(String username, boolean locked) {
        UserAccount ua = getByUsername(username);
        ua.setAccountNonLocked(!locked);
        ua.setUpdatedAt(Instant.now());
        return userRepo.save(ua);
    }

    public UserAccount getByPerson(String personId, PersonType personType) {
        return userRepo.findByPersonIdAndPersonType(personId, personType)
                .orElseThrow(() -> new IllegalArgumentException("No account for: " + personType + " " + personId));
    }

    // -------------------------
    // INTERNAL VALIDATION
    // -------------------------

    private String normalizeUsername(String username) {
        if (!StringUtils.hasText(username) || username.trim().length() < 3) {
            throw new IllegalArgumentException("Invalid username");
        }
        return username.trim().toLowerCase();
    }

    private void validatePassword(String rawPassword) {
        if (!StringUtils.hasText(rawPassword) || rawPassword.length() < 6) {
            throw new IllegalArgumentException("Password too short (min 6)");
        }
    }

    private void validateRolePersonLink(Set<Role> roles, String personId, PersonType personType) {
        boolean isStudent = roles.contains(Role.ROLE_STUDENT);
        boolean isTeacher = roles.contains(Role.ROLE_TEACHER);

        if (isStudent) {
            if (personType != PersonType.STUDENT || !StringUtils.hasText(personId))
                throw new IllegalArgumentException("STUDENT account must be linked to Student (personId, personType=STUDENT)");
        }

        if (isTeacher) {
            if (personType != PersonType.TEACHER || !StringUtils.hasText(personId))
                throw new IllegalArgumentException("TEACHER account must be linked to Teacher (personId, personType=TEACHER)");
        }

        // Manager/SuperAdmin can have null personId/personType
        boolean isManager = roles.contains(Role.ROLE_MANAGER) || roles.contains(Role.ROLE_SUPER_ADMIN);
        if (isManager) {
            // ok
        }
    }
    public List<UserAccount> getAllByRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }
        return userRepo.findByRolesContainingOrderByUsernameAsc(role);
    }
}
