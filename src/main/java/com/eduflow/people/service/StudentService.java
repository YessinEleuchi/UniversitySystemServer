package com.eduflow.people.service;

import com.eduflow.people.domain.Student;
import com.eduflow.people.repo.StudentRepository;
import com.eduflow.teaching.repo.ClassGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassGroupRepository classGroupRepository; // pour vérifier qu'on affecte à une classe existante

    public Student createStudent(Student student) {
        // petites validations
        if (!StringUtils.hasText(student.getMatricule())) {
            throw new IllegalArgumentException("Matricule is required");
        }
        studentRepository.findByMatricule(student.getMatricule())
                .ifPresent(s -> {
                    throw new IllegalStateException("Student with matricule " + student.getMatricule() + " already exists");
                });

        // si on a donné une classGroupId, vérifier qu'elle existe
        if (student.getClassGroupId() != null) {
            classGroupRepository.findById(student.getClassGroupId())
                    .orElseThrow(() -> new IllegalArgumentException("ClassGroup not found: " + student.getClassGroupId()));
        }

        return studentRepository.save(student);
    }

    public Student getStudent(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
    }

    public List<Student> getStudentsByClassGroup(String classGroupId) {
        return studentRepository.findByClassGroupId(classGroupId);
    }

    public Student assignStudentToClass(String studentId, String classGroupId) {
        Student student = getStudent(studentId);

        // vérifie que la classe existe
        classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new IllegalArgumentException("ClassGroup not found: " + classGroupId));

        student.setClassGroupId(classGroupId);
        return studentRepository.save(student);
    }
}
