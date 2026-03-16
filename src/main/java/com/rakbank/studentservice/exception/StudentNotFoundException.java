package com.rakbank.studentservice.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Student with StudentId:" + id + " not found.");
    }

    public StudentNotFoundException(String studentName) {
        super("Student with studentName:" + studentName + " not found.");
    }
}