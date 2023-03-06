package com.cms.student.service;

import com.cms.student.domain.entity.Student;
import com.cms.student.domain.request.StudentRequestDto;
import com.cms.student.domain.request.UpdateStudentRequestDto;
import com.cms.student.exception.ConnectionException;
import com.cms.student.exception.StudentException;
import com.cms.student.exception.InvalidLocationException;
import com.cms.student.exception.InvalidStudentException;
import com.cms.student.repository.StudentRepository;
import com.cms.student.wrapper.LocationResponseWrapper;
import com.cms.student.wrapper.ResponseWrapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class StudentService {
    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private final StudentRepository studentRepository;
    private final RestTemplate restTemplate;
    private static final String DEFAULT_SORT = "updated_at";
    private static final String LOCATION_ID_REPLACE_PHRASE = "##LOCATION-ID##";
    private final String getLocationUrl;

    @Autowired
    public StudentService(StudentRepository studentRepository, RestTemplate restTemplate,
                          @Value("${location.uri.baseUrl}") String baseUrl,
                          @Value("${location.uri.getLocationById}") String getLocation) {
        this.studentRepository = studentRepository;
        this.restTemplate = restTemplate;
        this.getLocationUrl = baseUrl + getLocation;
    }

    public Student createStudent(StudentRequestDto studentRequestDto) {
        try {
            Student student = new Student(studentRequestDto);
            var header = new HttpHeaders();
            var entity = new HttpEntity<String>(header);
            String uri = getLocationUrl.replace(LOCATION_ID_REPLACE_PHRASE, studentRequestDto.getLocationId());
            var responseWrapper = restTemplate.exchange(uri, HttpMethod.GET,
                    entity, LocationResponseWrapper.class);
            if (responseWrapper.getBody().getData() != null) {
                throw new InvalidLocationException("The selected location id not exists. Id : "
                        + studentRequestDto.getLocationId());
            }
            return studentRepository.save(student);
        } catch (ResourceAccessException e) {
            throw new ConnectionException("Can not access the resources from other services", e);
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                throw new InvalidLocationException("The selected location id not exists. Id : "
                        + studentRequestDto.getLocationId());
            }
            throw new StudentException("The rest template failed", e);
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

    public Student updateStudent(UpdateStudentRequestDto updateStudentRequestDto) {
        try {
            Student studentFromDB = getStudentById(updateStudentRequestDto.getStudentId());
            var header = new HttpHeaders();
            var entity = new HttpEntity<String>(header);
            String uri = getLocationUrl.replace(LOCATION_ID_REPLACE_PHRASE, updateStudentRequestDto.getLocationId());
            var responseWrapper = restTemplate.exchange(uri, HttpMethod.GET,
                    entity, LocationResponseWrapper.class);
            if (responseWrapper.getBody().getData() == null) {
                throw new InvalidLocationException("The selected location id not exists. Id : "
                        + updateStudentRequestDto.getLocationId());
            }
            studentFromDB.update(updateStudentRequestDto);
            studentRepository.save(studentFromDB);
            return studentFromDB;
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                throw new InvalidLocationException("The selected location id not exists. Id : "
                        + updateStudentRequestDto.getLocationId());
            }
            throw new StudentException("The rest template failed", e);
        }catch (DataAccessException e) {
            throw new StudentException("Updating student to database is failed", e);
        }
    }

    public void deleteStudent(String studentId) {
        try {
            Student studentFromDB = getStudentById(studentId);
            studentFromDB.setDeleted(true);
            studentFromDB.setUpdatedAt(System.currentTimeMillis());
            studentRepository.save(studentFromDB);
        } catch (DataAccessException e) {
            throw new StudentException("Deleting student is failed", e);
        }
    }
}
