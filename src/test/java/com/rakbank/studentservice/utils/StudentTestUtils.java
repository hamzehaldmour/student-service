package com.rakbank.studentservice.utils;

import com.rakbank.studentservice.dto.StudentRequest;
import com.rakbank.studentservice.dto.StudentResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StudentTestUtils {

    public static void assertStudentIds(List<StudentRequest> studentRequests, Set<String> expectedIds) {
        Set<String> actualIds = studentRequests.stream()
                .map(StudentRequest::studentId)
                .collect(Collectors.toSet());

        assertTrue(actualIds.containsAll(expectedIds), "Student Id do NOT match expected value!");
    }

    public static void assertStudentId(StudentResponse response, String expectedIds) {
        String actualIds = response.studentId();
        assertEquals(actualIds, expectedIds, "Student Id do NOT match expected value!");
    }
}
