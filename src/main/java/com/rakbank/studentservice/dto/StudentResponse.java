package com.rakbank.studentservice.dto;

public record StudentResponse(String studentId,
                              String studentName,
                              String grade,
                              String mobileNumber,
                              String schoolName) {
}

