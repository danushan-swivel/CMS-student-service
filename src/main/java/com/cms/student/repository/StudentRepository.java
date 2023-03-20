package com.cms.student.repository;

import com.cms.student.domain.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    @Query(value = "SELECT * FROM student d WHERE d.is_deleted=false", nativeQuery = true)
    Page<Student> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM student d WHERE d.is_deleted=false AND d.student_id=?1", nativeQuery = true)
    Optional<Student> findById(String studentId);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByFirstNameAndLastNameAndStudentIdNot(String firstName, String lastName, String studentId);

}
