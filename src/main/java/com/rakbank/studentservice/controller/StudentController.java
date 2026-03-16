package com.rakbank.studentservice.controller;

import com.rakbank.studentservice.dto.StudentRequest;
import com.rakbank.studentservice.dto.StudentResponse;
import com.rakbank.studentservice.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student API", description = "Student management APIs.")
public class StudentController {

    private static final String DEFAULT_MAX_NUMBER_STUDENTS_BY_GET_ALL = "200";

    private final StudentService studentService;

    ////
    /// Get Request ----------------------------------------------------------------------------------------------------
    //

    /**
     * Retrieves a paginated list of Students.
     *
     * @param page the page number for pagination (Default 0).
     * @param size the page size for pagination (Default 200).
     * @return a ResponseEntity containing the StudentsDTO of the requested Student.
     */
    @GetMapping
    @Operation(
            summary     = "Get all students (paginated)",
            description = "Returns a paginated list of students"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StudentResponse>> getAllStudents(
            @Parameter(description = "Page number (starting from 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of student per page", example = "20")
            @RequestParam(defaultValue = DEFAULT_MAX_NUMBER_STUDENTS_BY_GET_ALL) int size) {
        log.info("Received GET /api/students request");
        return ResponseEntity.ok(studentService.getAllStudentsPage(page, size));
    }

    /**
     * Retrieves a specific student by its student Id.
     *
     * @param studentId the student ID.
     * @return a ResponseEntity containing the StudentDTO of the requested Student.
     */
    @GetMapping("/{studentId}")
    @Operation(
            summary = "Get Student by ID",
            description = "Returns Student information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentResponse> getStudentByStudentId(
            @Parameter(description = "Student ID", required = true)
            @PathVariable String studentId) {
        log.info("Received GET /api/students/{} request", studentId);
        return ResponseEntity.ok(studentService.getStudentByStudentId(studentId));
    }

    ////
    /// Post Request ---------------------------------------------------------------------------------------------------
    //

    /**
     * Creates a new Student with the given details. Only 'admin' user is allowed.
     *
     * @param student the StudentDTO containing details to create the Student.
     * @return a ResponseEntity containing the StudentDTO of the requested Student.
     */
    @PostMapping
    @Operation(summary = "Create new Student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "student name conflict")
    })
    public ResponseEntity<StudentResponse> createStudent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Student object",
                    required = true)
            @RequestBody StudentRequest student) {
        log.info("Received POST /api/students request");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(student));
    }


    ////
    /// Delete Request -------------------------------------------------------------------------------------------------
    //

    /**
     * Deletes a Student by its ID.
     *
     * @param studentId the Student ID.
     * @return a ResponseEntity with a confirmation message.
     */
    @DeleteMapping("/{studentId}")
    @Operation(summary = "Delete Student")
    @ApiResponse(responseCode = "204", description = "Student deleted")
    public ResponseEntity<Void> deleteStudent(@PathVariable String studentId) {
        log.info("Received DELETE /api/students/{} request", studentId);
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}