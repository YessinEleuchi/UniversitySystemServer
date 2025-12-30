package com.eduflow.people.service;

import com.eduflow.people.domain.Student;
import com.eduflow.people.repo.StudentRepository;
import com.eduflow.teaching.repo.ClassGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassGroupRepository classGroupRepository;

    public Student createStudent(Student student) {
        validateForCreate(student);

        // matricule unique (ignore case recommandé)
        studentRepository.findByMatriculeIgnoreCase(student.getMatricule().trim())
                .ifPresent(s -> { throw new IllegalStateException("Student already exists with matricule: " + student.getMatricule()); });

        // email unique (ignore case)
        if (StringUtils.hasText(student.getEmail())) {
            studentRepository.findByEmailIgnoreCase(student.getEmail().trim())
                    .ifPresent(s -> { throw new IllegalStateException("Email already used: " + student.getEmail()); });
            student.setEmail(student.getEmail().trim().toLowerCase());
        }

        // verify class group if provided
        if (StringUtils.hasText(student.getClassGroupId())) {
            classGroupRepository.findById(student.getClassGroupId())
                    .orElseThrow(() -> new IllegalArgumentException("ClassGroup not found: " + student.getClassGroupId()));
        }

        student.setMatricule(student.getMatricule().trim().toUpperCase());
        return studentRepository.save(student);
    }



    public Student getStudent(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
    }

    public List<Student> getStudentsByClassGroup(String classGroupId) {
        if (!StringUtils.hasText(classGroupId)) return List.of();
        return studentRepository.findByClassGroupIdOrderByLastNameAscFirstNameAsc(classGroupId);
    }

    public Student assignStudentToClass(String studentId, String classGroupId) {
        Student student = getStudent(studentId);

        if (!StringUtils.hasText(classGroupId)) {
            student.setClassGroupId(null);
            return studentRepository.save(student);
        }

        classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new IllegalArgumentException("ClassGroup not found: " + classGroupId));

        student.setClassGroupId(classGroupId);
        return studentRepository.save(student);
    }

    private void validateForCreate(Student student) {
        if (student == null) throw new IllegalArgumentException("Student cannot be null");
        if (!StringUtils.hasText(student.getMatricule())) throw new IllegalArgumentException("Matricule is required");
        if (!StringUtils.hasText(student.getFirstName()) || !StringUtils.hasText(student.getLastName()))
            throw new IllegalArgumentException("First name and last name are required");
    }
    public List<Student> getAllStudents() {
        return studentRepository.findAllByOrderByLastNameAscFirstNameAsc();
    }

}
