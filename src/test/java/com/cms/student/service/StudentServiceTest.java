package com.cms.student.service;

import com.cms.student.domain.entity.Student;
import com.cms.student.domain.request.StudentRequestDto;
import com.cms.student.domain.request.UpdateStudentRequestDto;
import com.cms.student.domain.response.LocationResponseDto;
import com.cms.student.enums.Gender;
import com.cms.student.exception.*;
import com.cms.student.repository.StudentRepository;
import com.cms.student.wrapper.LocationResponseWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class StudentServiceTest {
    private static final String TUITION_CLASS_BASE_URL = "http://localhost:8105";
    private static final String GET_TUITION_CLASS_BY_ID_URL = "/api/v1/tuition/##LOCATION-ID##";

    private static final String STUDENT_ID = "sid-1254-7854-6485";
    private static final String ACCESS_TOKEN = "ey1365651-14156-51";
    private static final String FIRST_NAME = "Danushan";
    private static final String UPDATED_FIRST_NAME = "Danu";
    private static final String LAST_NAME = "Kanagasingam";
    private static final String UPDATED_LAST_NAME = "Danushan";
    private static final String ADDRESS = "A9 road, Vavuniya";
    private static final String GENDER = "Male";
    private static final String TUITION_CLASS_ID = "tid-1254-9654-7854-8955";
    private static final int PHONE_NUMBER = 771109101;
    private static final int AGE = 27;
    private static final int GRADE = 12;
    private static final int PAGE = 0;
    private static final int SIZE = 100;
    private static final String DEFAULT_SORT = "updated_at";

    private static final String TUITION_CLASS_ADDRESS = "Galle Road, Wellawatte";
    private static final String TUITION_CLASS_DISTRICT = "Colombo";
    private static final String TUITION_CLASS_PROVINCE = "South";

    private StudentService studentService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private RestTemplate restTemplate;


    @BeforeEach
    void setUp() {
        openMocks(this);
        studentService = new StudentService(studentRepository, restTemplate, TUITION_CLASS_BASE_URL,
                GET_TUITION_CLASS_BY_ID_URL);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_ReturnStudent_When_CreateAStudentSuccessfully() {
        Student student = getSampleStudent();
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        LocationResponseWrapper locationResponseWrapper = getSampleLocationResponseWrapper();
        when(studentRepository.existsByFirstNameAndLastName(FIRST_NAME, LAST_NAME))
                .thenReturn(false);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(LocationResponseWrapper.class))).thenReturn(ResponseEntity.of(Optional.of(locationResponseWrapper)));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        assertEquals(student, studentService.createStudent(studentRequestDto, ACCESS_TOKEN));
    }

    @Test
    void Should_ThrowStudentAlreadyExistsException_When_FirstNameAndLastNameAlreadyExistsInDatabase() {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        when(studentRepository.existsByFirstNameAndLastName(FIRST_NAME, LAST_NAME))
                .thenReturn(true);
        StudentAlreadyExistsException exception = assertThrows(StudentAlreadyExistsException.class, () ->
                studentService.createStudent(studentRequestDto, ACCESS_TOKEN));
        assertEquals("Student already exists", exception.getMessage());
    }

    @Test
    void Should_ThrowInvalidLocationException_When_TuitionCLassNotExistsInGivenId() {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        LocationResponseWrapper locationResponseWrapper = getSampleLocationResponseWrapper();
        locationResponseWrapper.setStatusCode(4030);
        locationResponseWrapper.setData(null);
        when(studentRepository.existsByFirstNameAndLastName(FIRST_NAME, LAST_NAME))
                .thenReturn(false);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(LocationResponseWrapper.class))).thenReturn(ResponseEntity.of(Optional.of(locationResponseWrapper)));
        InvalidLocationException exception = assertThrows(InvalidLocationException.class, () ->
                studentService.createStudent(studentRequestDto, ACCESS_TOKEN));
        assertEquals("The selected location id not exists. Id : " + TUITION_CLASS_ID, exception.getMessage());
    }

    @Test
    void Should_ThrowConnectionException_When_TuitionClassServiceIsNotRunning() {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        when(studentRepository.existsByFirstNameAndLastName(FIRST_NAME, LAST_NAME))
                .thenReturn(false);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(LocationResponseWrapper.class))).thenThrow(new ResourceAccessException("Couldn't access the resource"));
        ConnectionException exception = assertThrows(ConnectionException.class, () ->
                studentService.createStudent(studentRequestDto, ACCESS_TOKEN));
        assertEquals("Can not access the resources from other services", exception.getMessage());
    }

    @Test
    void Should_StudentException_When_GettingTuitionClassByIdIsFailed() {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        when(studentRepository.existsByFirstNameAndLastName(FIRST_NAME, LAST_NAME))
                .thenReturn(false);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(LocationResponseWrapper.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        StudentException exception = assertThrows(StudentException.class, () ->
                studentService.createStudent(studentRequestDto, ACCESS_TOKEN));
        assertEquals("The selected location id not exists. Id : " + TUITION_CLASS_ID, exception.getMessage());
    }

    @Test
    void Should_StudentException_When_SaveTheStudentIntoDatabaseIsFailed() {
        Student student = getSampleStudent();
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        LocationResponseWrapper locationResponseWrapper = getSampleLocationResponseWrapper();
        when(studentRepository.existsByFirstNameAndLastName(FIRST_NAME, LAST_NAME))
                .thenReturn(false);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(LocationResponseWrapper.class))).thenReturn(ResponseEntity.of(Optional.of(locationResponseWrapper)));
        when(studentRepository.save(any(Student.class))).thenThrow(new DataAccessException("ERROR") {
        });
        StudentException exception = assertThrows(StudentException.class, () ->
                studentService.createStudent(studentRequestDto, ACCESS_TOKEN));
        assertEquals("Saving student into database is failed", exception.getMessage());
    }

    @Test
    void Should_StudentException_When_CheckTheAlreadyExistenceStudentInDatabaseIsFailed() {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        when(studentRepository.existsByFirstNameAndLastName(FIRST_NAME, LAST_NAME)).thenThrow(new DataAccessException("ERROR") {
        });
        StudentException exception = assertThrows(StudentException.class, () ->
                studentService.createStudent(studentRequestDto, ACCESS_TOKEN));
        assertEquals("Checking the student record in database is failed", exception.getMessage());
    }

    @Test
    void Should_ReturnStudent_When_GetStudentByIdIsSuccessfully() {
        Student student = getSampleStudent();
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        assertEquals(student, studentService.getStudentById(STUDENT_ID));
    }

    @Test
    void Should_ThrowInvalidStudentException_When_StudentNotExistOnGivenId() {
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());
        InvalidStudentException exception = assertThrows(InvalidStudentException.class, () ->
                studentService.getStudentById(STUDENT_ID));
        assertEquals("The given student id is invalid" + STUDENT_ID, exception.getMessage());
    }

    @Test
    void Should_ThrowStudentException_When_StudentNotExistOnGivenId() {
        when(studentRepository.findById(STUDENT_ID)).thenThrow(new DataAccessException("ERROR") {
        });
        StudentException exception = assertThrows(StudentException.class, () ->
                studentService.getStudentById(STUDENT_ID));
        assertEquals("Retrieving student from database is failed", exception.getMessage());
    }

    @Test
    void Should_ReturnStudentPage_When_ValidPageableIsProvided() {
        Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.by(DEFAULT_SORT).ascending());
        Page<Student> studentPage = getSamplePage();
        when(studentRepository.findAll(pageable)).thenReturn(studentPage);
        assertEquals(studentPage, studentService.getStudentsPage());
    }

    @Test
    void Should_ThrowStudentException_When_GetStudentPageFromDatabaseIsFailed() {
        Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.by(DEFAULT_SORT).ascending());
        when(studentRepository.findAll(pageable)).thenThrow(new DataAccessException("ERROR") {
        });
        StudentException exception = assertThrows(StudentException.class, () ->
                studentService.getStudentsPage());
        assertEquals("Retrieving students details from database is failed", exception.getMessage());
    }

    @Test
    void Should_ReturnStudent_When_StudentUpdatedSuccessFully() {
        Student student = getSampleStudent();
        student.setFirstName(UPDATED_FIRST_NAME);
        student.setLastName(UPDATED_LAST_NAME);
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        LocationResponseWrapper locationResponseWrapper = getSampleLocationResponseWrapper();
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentRepository.existsByFirstNameAndLastNameAndStudentIdNot(UPDATED_FIRST_NAME, UPDATED_LAST_NAME, STUDENT_ID))
                .thenReturn(false);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(LocationResponseWrapper.class))).thenReturn(ResponseEntity.of(Optional.of(locationResponseWrapper)));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        assertEquals(student, studentService.updateStudent(updateStudentRequestDto, ACCESS_TOKEN));
    }

    @Test
    void Should_ThrowStudentAlreadyExistsException_When_FirstNameAndLastNameAlreadyExistsInDatabaseForUpdateStudent() {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        Student student = getSampleStudent();
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentRepository.existsByFirstNameAndLastNameAndStudentIdNot(UPDATED_FIRST_NAME, UPDATED_LAST_NAME, STUDENT_ID))
                .thenReturn(true);
        StudentAlreadyExistsException exception = assertThrows(StudentAlreadyExistsException.class, () ->
                studentService.updateStudent(updateStudentRequestDto, ACCESS_TOKEN));
        assertEquals("Student already exists", exception.getMessage());
    }

    @Test
    void Should_ThrowInvalidLocationException_When_TuitionCLassNotExistsInGivenIdForUpdateStudent() {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        LocationResponseWrapper locationResponseWrapper = getSampleLocationResponseWrapper();
        locationResponseWrapper.setStatusCode(4030);
        locationResponseWrapper.setData(null);
        Student student = getSampleStudent();
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentRepository.existsByFirstNameAndLastNameAndStudentIdNot(UPDATED_FIRST_NAME, UPDATED_LAST_NAME, STUDENT_ID))
                .thenReturn(false);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(LocationResponseWrapper.class))).thenReturn(ResponseEntity.of(Optional.of(locationResponseWrapper)));
        InvalidLocationException exception = assertThrows(InvalidLocationException.class, () ->
                studentService.updateStudent(updateStudentRequestDto, ACCESS_TOKEN));
        assertEquals("The selected location id not exists. Id : " + TUITION_CLASS_ID, exception.getMessage());
    }

    @Test
    void Should_ThrowConnectionException_When_TuitionClassServiceIsNotRunningForUpdateStudent() {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        Student student = getSampleStudent();
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentRepository.existsByFirstNameAndLastNameAndStudentIdNot(UPDATED_FIRST_NAME, UPDATED_LAST_NAME, STUDENT_ID))
                .thenReturn(false);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(LocationResponseWrapper.class))).thenThrow(new ResourceAccessException("Couldn't access the resource"));
        ConnectionException exception = assertThrows(ConnectionException.class, () ->
                studentService.updateStudent(updateStudentRequestDto, ACCESS_TOKEN));
        assertEquals("Can not access the resources from other services", exception.getMessage());
    }

    @Test
    void Should_StudentException_When_GettingTuitionClassByIdIsFailedForUpdateStudent() {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        Student student = getSampleStudent();
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentRepository.existsByFirstNameAndLastNameAndStudentIdNot(UPDATED_FIRST_NAME, UPDATED_LAST_NAME, STUDENT_ID))
                .thenReturn(false);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(LocationResponseWrapper.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        StudentException exception = assertThrows(StudentException.class, () ->
                studentService.updateStudent(updateStudentRequestDto, ACCESS_TOKEN));
        assertEquals("The selected location id not exists. Id : " + TUITION_CLASS_ID, exception.getMessage());
    }

    @Test
    void Should_StudentException_When_SaveTheStudentIntoDatabaseIsFailedForUpdateStudent() {
        Student student = getSampleStudent();
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        LocationResponseWrapper locationResponseWrapper = getSampleLocationResponseWrapper();
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentRepository.existsByFirstNameAndLastNameAndStudentIdNot(UPDATED_FIRST_NAME, UPDATED_LAST_NAME, STUDENT_ID))
                .thenReturn(false);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                eq(LocationResponseWrapper.class))).thenReturn(ResponseEntity.of(Optional.of(locationResponseWrapper)));
        when(studentRepository.save(any(Student.class))).thenThrow(new DataAccessException("ERROR") {
        });
        StudentException exception = assertThrows(StudentException.class, () ->
                studentService.updateStudent(updateStudentRequestDto, ACCESS_TOKEN));
        assertEquals("Updating student to database is failed", exception.getMessage());
    }

    @Test
    void Should_StudentException_When_CheckTheAlreadyExistenceStudentInDatabaseIsFailedForUpdateStudent() {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        Student student = getSampleStudent();
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentRepository.existsByFirstNameAndLastNameAndStudentIdNot(UPDATED_FIRST_NAME, UPDATED_LAST_NAME, STUDENT_ID))
                .thenThrow(new DataAccessException("ERROR") {
                });
        StudentException exception = assertThrows(StudentException.class, () ->
                studentService.updateStudent(updateStudentRequestDto, ACCESS_TOKEN));
        assertEquals("Checking the student record in database is failed", exception.getMessage());
    }

    @Test
    void Should_DeleteStudent_When_StudentIdIsProvided() {
        Student student = getSampleStudent();
        student.setDeleted(true);
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        studentService.deleteStudent(STUDENT_ID);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void Should_ThrowInvalidStudentException_When_DeleteStudentIsFailed() {
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());
        InvalidStudentException exception = assertThrows(InvalidStudentException.class, () ->
                studentService.deleteStudent(STUDENT_ID));
        assertEquals("The given student id is invalid" + STUDENT_ID, exception.getMessage());
    }


    @Test
    void Should_ThrowStudentException_When_GetStudentIsFailedForDelete() {
        Student student = getSampleStudent();
        student.setDeleted(true);
        when(studentRepository.findById(STUDENT_ID)).thenThrow(new DataAccessException("ERROR") { });
        when(studentRepository.save(student)).thenThrow(new DataAccessException("ERROR") {  });
        StudentException exception = assertThrows(StudentException.class, () ->
                studentService.deleteStudent(STUDENT_ID));
        assertEquals("Retrieving student from database is failed", exception.getMessage());
    }

    @Test
    void Should_ThrowStudentException_When_DeleteStudentIsFailed() {
        Student student = getSampleStudent();
        student.setDeleted(true);
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenThrow(new DataAccessException("ERROR") {  });
        StudentException exception = assertThrows(StudentException.class, () ->
                studentService.deleteStudent(STUDENT_ID));
        assertEquals("Deleting student is failed", exception.getMessage());
    }

    /**
     * This method creates sample student
     *
     * @return Student
     */
    private Student getSampleStudent() {
        Student student = new Student();
        student.setAddress(ADDRESS);
        student.setDeleted(false);
        student.setAge(AGE);
        student.setGrade(GRADE);
        student.setPhoneNumber(PHONE_NUMBER);
        student.setGender(Gender.valueOf(GENDER.toUpperCase()));
        student.setFirstName(FIRST_NAME);
        student.setLastName(LAST_NAME);
        student.setJoinedDate(new Date(System.currentTimeMillis()));
        student.setTuitionClassId(TUITION_CLASS_ID);
        return student;
    }

    /**
     * This method creates sample student request dto
     *
     * @return StudentRequestDto
     */
    private StudentRequestDto getSampleStudentRequestDto() {
        StudentRequestDto studentRequestDto = new StudentRequestDto();
        studentRequestDto.setAddress(ADDRESS);
        studentRequestDto.setAge(AGE);
        studentRequestDto.setGrade(GRADE);
        studentRequestDto.setPhoneNumber(PHONE_NUMBER);
        studentRequestDto.setGender(GENDER);
        studentRequestDto.setFirstName(FIRST_NAME);
        studentRequestDto.setLastName(LAST_NAME);
        studentRequestDto.setTuitionClassId(TUITION_CLASS_ID);
        return studentRequestDto;
    }

    /**
     * This method creates sample location response wrapper
     *
     * @return LocationResponseWrapper
     */
    private LocationResponseWrapper getSampleLocationResponseWrapper() {
        LocationResponseDto locationResponseDto = getSampleLocationResponseDto();
        LocationResponseWrapper locationResponseWrapper = new LocationResponseWrapper();
        locationResponseWrapper.setData(locationResponseDto);
        locationResponseWrapper.setStatusCode(HttpStatus.OK.value());
        locationResponseWrapper.setMessage("Location retrieved successfully");
        return locationResponseWrapper;
    }

    /**
     * This method creates sample location responseDto
     *
     * @return LocationResponseDto
     */
    private LocationResponseDto getSampleLocationResponseDto() {
        LocationResponseDto locationResponseDto = new LocationResponseDto();
        locationResponseDto.setLocationId(TUITION_CLASS_ID);
        locationResponseDto.setAddress(TUITION_CLASS_ADDRESS);
        locationResponseDto.setDistrict(TUITION_CLASS_DISTRICT);
        locationResponseDto.setProvince(TUITION_CLASS_PROVINCE);
        locationResponseDto.setDeleted(false);
        return locationResponseDto;
    }

    /**
     * This method creates sample student page
     *
     * @return StudentPage
     */
    private Page<Student> getSamplePage() {
        Student student = getSampleStudent();
        List<Student> studentList = new ArrayList<>();
        studentList.add(student);
        return new PageImpl<>(studentList);
    }

    /**
     * Thos method creates sample update student request dto
     *
     * @return UpdateStudentRequestDto
     */
    private UpdateStudentRequestDto getSampleUpdateStudentRequestDto() {
        UpdateStudentRequestDto updateStudentRequestDto = new UpdateStudentRequestDto();
        updateStudentRequestDto.setStudentId(STUDENT_ID);
        updateStudentRequestDto.setAddress(ADDRESS);
        updateStudentRequestDto.setAge(AGE);
        updateStudentRequestDto.setGrade(GRADE);
        updateStudentRequestDto.setPhoneNumber(PHONE_NUMBER);
        updateStudentRequestDto.setGender(GENDER);
        updateStudentRequestDto.setFirstName(UPDATED_FIRST_NAME);
        updateStudentRequestDto.setLastName(UPDATED_LAST_NAME);
        updateStudentRequestDto.setTuitionClassId(TUITION_CLASS_ID);
        return updateStudentRequestDto;
    }

}