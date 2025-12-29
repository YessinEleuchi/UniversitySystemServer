package com.eduflow.people.service;

import com.eduflow.people.domain.Teacher;
import com.eduflow.people.repo.TeacherRepository;
import com.eduflow.teaching.repo.CourseInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseInstanceRepository courseInstanceRepository;

    public Teacher createTeacher(Teacher teacher) {
        validateTeacherForCreation(teacher);

        teacherRepository.findByCodeIgnoreCase(teacher.getCode().trim())
                .ifPresent(t -> { throw new IllegalStateException("Teacher already exists with code: " + teacher.getCode()); });

        if (StringUtils.hasText(teacher.getEmail())) {
            teacherRepository.findByEmailIgnoreCase(teacher.getEmail().trim())
                    .ifPresent(t -> { throw new IllegalStateException("Email already used: " + teacher.getEmail()); });
            teacher.setEmail(teacher.getEmail().trim().toLowerCase());
        }

        teacher.setCode(teacher.getCode().trim().toUpperCase());
        teacher.setFirstName(teacher.getFirstName().trim());
        teacher.setLastName(teacher.getLastName().trim());

        return teacherRepository.save(teacher);
    }

    public Teacher getTeacher(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + id));
    }

    public List<Teacher> getTeachersByDepartment(String department) {
        if (!StringUtils.hasText(department)) throw new IllegalArgumentException("Department is required");
        return teacherRepository.findByDepartmentOrderByLastNameAscFirstNameAsc(department.trim());
    }

    public Teacher updateTeacherDepartment(String teacherId, String newDepartment) {
        Teacher teacher = getTeacher(teacherId);
        if (!StringUtils.hasText(newDepartment)) throw new IllegalArgumentException("New department is required");

        if (!newDepartment.trim().equals(teacher.getDepartment())) {
            teacher.setDepartment(newDepartment.trim());
        }
        return teacherRepository.save(teacher);
    }

    public Optional<Teacher> findByCode(String code) {
        if (!StringUtils.hasText(code)) return Optional.empty();
        return teacherRepository.findByCodeIgnoreCase(code.trim());
    }

    public List<Teacher> searchByName(String query) {
        if (!StringUtils.hasText(query) || query.trim().length() < 2) return List.of();
        String q = query.trim();
        return teacherRepository.findTop10ByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(q, q);
    }



    public List<Teacher> getAllTeachers() {
        return teacherRepository.findByOrderByLastNameAscFirstNameAsc();
    }

    private void validateTeacherForCreation(Teacher teacher) {
        if (teacher == null) throw new IllegalArgumentException("Teacher cannot be null");
        if (!StringUtils.hasText(teacher.getCode())) throw new IllegalArgumentException("Teacher code is required");
        if (!StringUtils.hasText(teacher.getFirstName()) || !StringUtils.hasText(teacher.getLastName()))
            throw new IllegalArgumentException("First name and last name are required");
    }
}
