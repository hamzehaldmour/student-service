package com.rakbank.studentservice.repository;

import com.rakbank.studentservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentId(String StudentId);
    boolean existsByStudentName(String StudentName);

}
