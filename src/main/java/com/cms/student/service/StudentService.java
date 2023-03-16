package com.cms.student.service;

import com.cms.student.domain.entity.Student;
import com.cms.student.domain.request.StudentRequestDto;
import com.cms.student.domain.request.UpdateStudentRequestDto;
import com.cms.student.exception.*;
import com.cms.student.repository.StudentRepository;
import com.cms.student.utills.Constants;
import com.cms.student.wrapper.LocationResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.Objects;

@Service
public class StudentService {
    private static final int PAGE = 0;
    private static final int SIZE = 100;
    private static final String DEFAULT_SORT = "updated_at";
    private static final String LOCATION_ID_REPLACE_PHRASE = "##LOCATION-ID##";
    private static final String INVALID_TUITION_CLASS_EXCEPTION_MESSAGE = "The selected location id not exists. Id : ";
    private final StudentRepository studentRepository;
    private final RestTemplate restTemplate;
    private final String getLocationUrl;

    @Autowired
    public StudentService(StudentRepository studentRepository, RestTemplate restTemplate,
                          @Value("${location.uri.baseUrl}") String baseUrl,
                          @Value("${location.uri.getLocationById}") String getLocation) {
        this.studentRepository = studentRepository;
        this.restTemplate = restTemplate;
        this.getLocationUrl = baseUrl + getLocation;
    }

    public Student createStudent(StudentRequestDto studentRequestDto, String authToken) {
        try {
            Student student = new Student(studentRequestDto);
            if (checkStudentExistence(studentRequestDto.getFirstName(), studentRequestDto.getLastName(), null)) {
                throw new StudentAlreadyExistsException("Student already exists");
            }
            var header = new HttpHeaders();
            header.set(Constants.TOKEN_HEADER, authToken.trim());
            var entity = new HttpEntity<String>(header);
            String uri = getLocationUrl.replace(LOCATION_ID_REPLACE_PHRASE, studentRequestDto.getTuitionClassId());
            var responseWrapper = restTemplate.exchange(uri, HttpMethod.GET,
                    entity, LocationResponseWrapper.class);
            var statusCode = Objects.requireNonNull(responseWrapper.getBody()).getStatusCode();
            if (statusCode != 2032) {
                throw new InvalidLocationException(INVALID_TUITION_CLASS_EXCEPTION_MESSAGE
                        + studentRequestDto.getTuitionClassId());
            }
            return studentRepository.save(student);
        } catch (ResourceAccessException e) {
            throw new ConnectionException("Can not access the resources from other services", e);
        } catch (HttpClientErrorException e) {
            throw new StudentException("Getting tuition class by id is failed", e);
        } catch (DataAccessException e) {
            throw new StudentException("Saving student into database is failed", e);
        }
    }

    public Student getStudentById(String studentId) {
        try {
            var optionalStudent = studentRepository.findById(studentId);
            if (optionalStudent.isEmpty()) {
                throw new InvalidStudentException("The given student id is invalid" + studentId);
            }
            return optionalStudent.get();
        } catch (DataAccessException e) {
            throw new StudentException("Retrieving student from database is failed", e);
        }
    }

    public Page<Student> getStudentsPage() {
        try {
            Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.by(DEFAULT_SORT).ascending());
            return studentRepository.findAll(pageable);
        } catch (DataAccessException e) {
            throw new StudentException("Retrieving students details from database is failed", e);
        }
    }

    public Student updateStudent(UpdateStudentRequestDto updateStudentRequestDto, String authToken) {
        try {
            Student studentFromDB = getStudentById(updateStudentRequestDto.getStudentId());
            if (checkStudentExistence(updateStudentRequestDto.getFirstName(), updateStudentRequestDto.getLastName(),
                    updateStudentRequestDto.getStudentId())) {
                throw new StudentAlreadyExistsException("Student already exists");
            }
            var header = new HttpHeaders();
            header.set(Constants.TOKEN_HEADER, authToken.trim());
            var entity = new HttpEntity<String>(header);
            String uri = getLocationUrl.replace(LOCATION_ID_REPLACE_PHRASE, updateStudentRequestDto.getTuitionClassId());
            var responseWrapper = restTemplate.exchange(uri, HttpMethod.GET,
                    entity, LocationResponseWrapper.class);
            var statusCode = Objects.requireNonNull(responseWrapper.getBody()).getStatusCode();
            if (statusCode != 2032) {
                throw new InvalidLocationException(INVALID_TUITION_CLASS_EXCEPTION_MESSAGE
                        + updateStudentRequestDto.getTuitionClassId());
            }
            studentFromDB.update(updateStudentRequestDto);
            studentRepository.save(studentFromDB);
            return studentFromDB;
        } catch (ResourceAccessException e) {
            throw new ConnectionException("Can not access the resources from other services", e);
        } catch (HttpClientErrorException e) {
            throw new StudentException("Getting tuition class by id is failed", e);
        } catch (DataAccessException e) {
            throw new StudentException("Updating student to database is failed", e);
        }
    }

    public void deleteStudent(String studentId) {
        try {
            Student studentFromDB = getStudentById(studentId);
            studentFromDB.setDeleted(true);
            studentFromDB.setUpdatedAt(new Date(System.currentTimeMillis()));
            studentRepository.save(studentFromDB);
        } catch (DataAccessException e) {
            throw new StudentException("Deleting student is failed", e);
        }
    }

    private boolean checkStudentExistence(String firstName, String lastName, String studentId) {
        try {
            if (studentId == null) {
                return studentRepository.existsByFirstNameAndLastName(firstName, lastName);
            } else {
                return studentRepository.existsByFirstNameAndLastNameAndStudentId(firstName, lastName, studentId);
            }
        } catch (DataAccessException e) {
            throw new StudentException("Checking the student record in database is failed");
        }
    }
}
