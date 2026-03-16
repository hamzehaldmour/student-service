CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL UNIQUE,
    student_name VARCHAR(255) NOT NULL,
    grade VARCHAR(20),
    mobile_number VARCHAR(20),
    school_name VARCHAR(255)
);

CREATE INDEX idx_student_id ON students(student_id);

