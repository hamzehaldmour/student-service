package com.rakbank.studentservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerUT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateStudent() throws Exception {

        String requestBody = """
        {
          "studentId": "TEST_S1001",
          "studentName": "ali",
          "grade": "Grade 10",
          "mobileNumber": "0790000000",
          "schoolName": "International School"
        }
        """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value("TEST_S1001"))
                .andExpect(jsonPath("$.studentName").value("ali"));
    }

    @Test
    void shouldGetStudents() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteStudent() throws Exception {
        mockMvc.perform(delete("/api/students/TEST_S1001"))
                .andExpect(status().isNoContent());
    }
}
