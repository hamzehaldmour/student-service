package com.rakbank.studentservice.service;

import com.rakbank.studentservice.dto.StudentRequest;
import com.rakbank.studentservice.dto.StudentResponse;
import com.rakbank.studentservice.entity.Student;
import com.rakbank.studentservice.exception.DuplicateStudentException;
import com.rakbank.studentservice.exception.StudentNotFoundException;
import com.rakbank.studentservice.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    private StudentResponse mapToResponse(Student student) {
        return new StudentResponse(
                student.getStudentId(),
                student.getStudentName(),
                student.getGrade(),
                student.getMobileNumber(),
                student.getSchoolName()
        );
    }

    public Page<StudentResponse> getAllStudentsPage(int page, int size) {
        log.info("Fetching student: page={}, size={}", page, size);
        int pageSize = Math.min(size, 200);
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<StudentResponse> studentResponsePage = studentRepository.findAll(pageable)
                .map(this::mapToResponse);
        log.debug("Found {} Student for page {}", studentResponsePage.getContent().size(), page);
        return studentResponsePage;
    }

    public StudentResponse createStudent(StudentRequest studentRequest) {
        log.info("Create student with studentId:{}", studentRequest.studentId());

        if (studentRepository.existsByStudentName(studentRequest.studentName())) {
            log.warn("Student with studentName already exist:{}!", studentRequest.studentName());
            throw new DuplicateStudentException(studentRequest.studentName());
        }

        Student student = new Student();

        student.setStudentId(studentRequest.studentId());
        student.setStudentName(studentRequest.studentName());
        student.setGrade(studentRequest.grade());
        student.setMobileNumber(studentRequest.mobileNumber());
        student.setSchoolName(studentRequest.schoolName());

        Student saved = studentRepository.save(student);
        log.debug("Saved student with studentId:{} successfully", saved.getStudentId());

        return new StudentResponse(saved.getStudentId(),
                saved.getStudentName(),
                saved.getGrade(),
                saved.getMobileNumber(),
                saved.getSchoolName());
    }

    public StudentResponse getStudentByStudentId(String studentId) {
        log.info("Get student by studentId:{}", studentId);
        Student student = studentRepository
                .findByStudentId(studentId)
                .orElseThrow(() -> {
                    log.warn("Student not found:{}", studentId);
                    return new StudentNotFoundException(studentId);
                });
        log.debug("Found student with studentId:{}", studentId);
        return mapToResponse(student);
    }

    public void deleteStudent(String studentId) {
        log.info("Deleting studentId:{}", studentId);
        Student student = studentRepository
                .findByStudentId(studentId)
                .orElseThrow(() -> {
                    log.warn("Student not found:{}", studentId);
                    return new StudentNotFoundException(studentId);
                });
        log.debug("Deleting studentId:{}", studentId);
        studentRepository.delete(student);
    }
}
