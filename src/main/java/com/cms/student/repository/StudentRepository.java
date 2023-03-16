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

//    @Query(value = "SELECT COUNT(*) FROM student d WHERE d.is_deleted=false AND d.first_name=?1 AND d.last_name=?2", nativeQuery = true)
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

//    @Query(value = "SELECT COUNT(*)>0 FROM student d WHERE d.is_deleted=false AND d.first_name=?1 AND d.last_name=?2 AND d.student_id!=?3", nativeQuery = true)
    boolean existsByFirstNameAndLastNameAndStudentId(String firstName, String lastName, String studentId);

}
