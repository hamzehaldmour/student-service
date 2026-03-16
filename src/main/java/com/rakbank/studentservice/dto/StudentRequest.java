package com.rakbank.studentservice.dto;

public record StudentRequest(String studentId,
                             String studentName,
                             String grade,
                             String mobileNumber,
                             String schoolName) {
}
