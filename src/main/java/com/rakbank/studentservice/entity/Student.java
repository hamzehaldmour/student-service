package com.rakbank.studentservice.entity;

import com.rakbank.studentservice.entity.internal.StudentTable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = StudentTable.TABLE_NAME)
@Setter
@Getter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = StudentTable.ID)
    private Long id;

    @Column(name = StudentTable.STUDENT_ID)
    private String studentId;
    @Column(name = StudentTable.STUDENT_NAME)
    private String studentName;
    @Column(name = StudentTable.GRADE)
    private String grade;
    @Column(name = StudentTable.MOBILE_NUMBER)
    private String mobileNumber;
    @Column(name = StudentTable.SCHOOL_NAME)
    private String schoolName;
}
