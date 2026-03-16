package com.rakbank.studentservice.exception;

public class DuplicateStudentException extends RuntimeException {

    public DuplicateStudentException(Long id) {
        super("Student with StudentId:" + id + " already Exists.");
    }

    public DuplicateStudentException(String studentName) {
        super("Student with studentName:" + studentName + " already Exists.");
    }
}
