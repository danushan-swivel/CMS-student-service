package com.cms.student.controller;

import com.cms.student.domain.entity.Student;
import com.cms.student.domain.request.StudentRequestDto;
import com.cms.student.domain.request.UpdateStudentRequestDto;
import com.cms.student.enums.ErrorResponseStatus;
import com.cms.student.enums.Gender;
import com.cms.student.enums.StudentStatus;
import com.cms.student.enums.SuccessResponseStatus;
import com.cms.student.exception.*;
import com.cms.student.service.StudentService;
import com.cms.student.utills.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StudentControllerTest {
    private static final String STUDENT_BASE_URL = "/api/v1/student";
    private static final String STUDENT_BY_ID_URL = "/api/v1/student/##STUDENT-ID##";
    private static final String STUDENT_ID = "sid-1254-7854-6485";
    private static final String ACCESS_TOKEN = "ey1365651-14156-51";
    private static final String FIRST_NAME = "Danushan";
    private static final String UPDATED_FIRST_NAME = "Danu";
    private static final String LAST_NAME = "Kanagasingam";
    private static final String UPDATED_LAST_NAME = "Danushan";
    private static final String ADDRESS = "A9 road, Vavuniya";
    private static final String GENDER = "Male";
    private static final String TUITION_CLASS_ID = "tid-1254-9654-7854-8955";
    private static final String REPLACE_STUDENT_ID = "##STUDENT-ID##";
    private static final int PHONE_NUMBER = 771109101;
    private static final int AGE = 27;
    private static final int GRADE = 12;

    @Mock
    private StudentService studentService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        StudentController studentController = new StudentController(studentService);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_ReturnOk_When_CreateStudentSuccessfully() throws Exception {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        Student student = getSampleStudent();
        when(studentService.createStudent(any(StudentRequestDto.class), anyString())).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders.post(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(studentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatus.STUDENT_CREATED.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatus.STUDENT_CREATED.getStatusCode()))
                .andExpect(jsonPath("$.data.studentId", startsWith("sid-")));
    }

    @Test
    void Should_ReturnBadRequest_When_RequiredFieldsAreNotAvailableForCreateStudent() throws Exception {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        studentRequestDto.setFirstName("");
        mockMvc.perform(MockMvcRequestBuilders.post(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(studentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.MISSING_REQUIRED_FIELDS.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnBadRequest_When_InvalidAgeIsProvidedForCreateStudent() throws Exception {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        studentRequestDto.setAge(-1);
        mockMvc.perform(MockMvcRequestBuilders.post(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(studentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INVALID_AGE.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INVALID_AGE.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnBadRequest_When_StudentAlreadyExistsForCreateStudent() throws Exception {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        doThrow(new StudentAlreadyExistsException("ERROR")).when(studentService).createStudent(any(StudentRequestDto.class), anyString());
        mockMvc.perform(MockMvcRequestBuilders.post(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(studentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.STUDENT_ALREADY_EXISTS.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.STUDENT_ALREADY_EXISTS.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnBadRequest_When_InvalidTuitionClassIdIsProvidedForCreateStudent() throws Exception {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        doThrow(new InvalidLocationException("ERROR")).when(studentService).createStudent(any(StudentRequestDto.class), anyString());
        mockMvc.perform(MockMvcRequestBuilders.post(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(studentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INVALID_TUITION_CLASS_LOCATION.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INVALID_TUITION_CLASS_LOCATION.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnBadRequest_When_TuitionClassServiceNotAvailableForCreateStudent() throws Exception {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        doThrow(new ConnectionException("ERROR")).when(studentService).createStudent(any(StudentRequestDto.class), anyString());
        mockMvc.perform(MockMvcRequestBuilders.post(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(studentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INTER_CONNECTION_FAILED.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INTER_CONNECTION_FAILED.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnInternalServerError_When_CreateStudentIsFailed() throws Exception {
        StudentRequestDto studentRequestDto = getSampleStudentRequestDto();
        doThrow(new StudentException("ERROR")).when(studentService).createStudent(any(StudentRequestDto.class), anyString());
        mockMvc.perform(MockMvcRequestBuilders.post(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(studentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INTERNAL_SERVER_ERROR.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnOk_When_GetStudentSuccessfully() throws Exception {
        String url = STUDENT_BY_ID_URL.replace(REPLACE_STUDENT_ID, STUDENT_ID);
        Student student = getSampleStudent();
        when(studentService.getStudentById(STUDENT_ID)).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatus.READ_STUDENT.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatus.READ_STUDENT.getStatusCode()))
                .andExpect(jsonPath("$.data.studentId", startsWith("sid-")));
    }

    @Test
    void Should_ReturnBadRequest_When_InvalidStudentIdIsProvided() throws Exception {
        String url = STUDENT_BY_ID_URL.replace(REPLACE_STUDENT_ID, STUDENT_ID);
        doThrow(new InvalidStudentException("ERROR")).when(studentService).getStudentById(STUDENT_ID);
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INVALID_STUDENT.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INVALID_STUDENT.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnInternalServerError_When_GetStudentByIdIsFailed() throws Exception {
        String url = STUDENT_BY_ID_URL.replace(REPLACE_STUDENT_ID, STUDENT_ID);
        doThrow(new StudentException("ERROR")).when(studentService).getStudentById(STUDENT_ID);
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INTERNAL_SERVER_ERROR.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnOk_When_GetAllStudentDetailsSuccessfully() throws Exception {
        Page<Student> studentPage = getSamplePage();
        when(studentService.getStudentsPage()).thenReturn(studentPage);
        mockMvc.perform(MockMvcRequestBuilders.get(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatus.READ_STUDENT_LIST.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatus.READ_STUDENT_LIST.getStatusCode()))
                .andExpect(jsonPath("$.data.students[0].studentId", startsWith("sid-")));
    }

    @Test
    void Should_ReturnInternalServerError_When_GetStudentsDetailsIsFailed() throws Exception {
        doThrow(new StudentException("ERROR")).when(studentService).getStudentsPage();
        mockMvc.perform(MockMvcRequestBuilders.get(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INTERNAL_SERVER_ERROR.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    //
    @Test
    void Should_ReturnOk_When_UpdateStudentSuccessfully() throws Exception {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        Student student = getSampleStudent();
        student.setFirstName(UPDATED_FIRST_NAME);
        student.setFirstName(UPDATED_FIRST_NAME);
        when(studentService.updateStudent(any(UpdateStudentRequestDto.class), anyString())).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders.put(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(updateStudentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatus.STUDENT_UPDATES.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatus.STUDENT_UPDATES.getStatusCode()))
                .andExpect(jsonPath("$.data.studentId", startsWith("sid-")));
    }

    @Test
    void Should_ReturnBadRequest_When_RequiredFieldsAreNotAvailableForUpdateStudent() throws Exception {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        updateStudentRequestDto.setLastName(" ");
        mockMvc.perform(MockMvcRequestBuilders.put(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(updateStudentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.MISSING_REQUIRED_FIELDS.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnBadRequest_When_InvalidAgeIsProvidedForUpdateStudent() throws Exception {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        updateStudentRequestDto.setAge(-1);
        mockMvc.perform(MockMvcRequestBuilders.put(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(updateStudentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INVALID_AGE.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INVALID_AGE.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnBadRequest_When_InvalidStudentIdIsProvidedForUpdateStudent() throws Exception {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        doThrow(new InvalidStudentException("ERROR")).when(studentService).updateStudent(any(UpdateStudentRequestDto.class), anyString());
        mockMvc.perform(MockMvcRequestBuilders.put(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(updateStudentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INVALID_STUDENT.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INVALID_STUDENT.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnBadRequest_When_StudentAlreadyExistsForUpdateStudent() throws Exception {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        doThrow(new StudentAlreadyExistsException("ERROR")).when(studentService).updateStudent(any(UpdateStudentRequestDto.class), anyString());
        mockMvc.perform(MockMvcRequestBuilders.put(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(updateStudentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.STUDENT_ALREADY_EXISTS.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.STUDENT_ALREADY_EXISTS.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnBadRequest_When_InvalidTuitionClassIdIsProvidedForUpdateStudent() throws Exception {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        doThrow(new InvalidLocationException("ERROR")).when(studentService).updateStudent(any(UpdateStudentRequestDto.class), anyString());
        mockMvc.perform(MockMvcRequestBuilders.put(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(updateStudentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INVALID_TUITION_CLASS_LOCATION.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INVALID_TUITION_CLASS_LOCATION.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnBadRequest_When_TuitionClassServiceNotAvailableForUpdateStudent() throws Exception {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        doThrow(new ConnectionException("ERROR")).when(studentService).updateStudent(any(UpdateStudentRequestDto.class), anyString());
        mockMvc.perform(MockMvcRequestBuilders.put(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(updateStudentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INTER_CONNECTION_FAILED.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INTER_CONNECTION_FAILED.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnInternalServerError_When_UpdateStudentIsFailed() throws Exception {
        UpdateStudentRequestDto updateStudentRequestDto = getSampleUpdateStudentRequestDto();
        doThrow(new StudentException("ERROR")).when(studentService).updateStudent(any(UpdateStudentRequestDto.class), anyString());
        mockMvc.perform(MockMvcRequestBuilders.put(STUDENT_BASE_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(updateStudentRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INTERNAL_SERVER_ERROR.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnOk_When_StudentIsDeletedSuccessfully() throws Exception {
        doNothing().when(studentService).deleteStudent(STUDENT_ID);
        String url = STUDENT_BY_ID_URL.replace(REPLACE_STUDENT_ID, STUDENT_ID);
        mockMvc.perform(MockMvcRequestBuilders.delete(url)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatus.STUDENT_DELETED.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatus.STUDENT_DELETED.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnBadRequest_When_InvalidStudentIdIsProvidedForDeleteStudent() throws Exception {
        doThrow(new InvalidStudentException("ERROR")).when(studentService).deleteStudent(STUDENT_ID);
        String url = STUDENT_BY_ID_URL.replace(REPLACE_STUDENT_ID, STUDENT_ID);
        mockMvc.perform(MockMvcRequestBuilders.delete(url)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INVALID_STUDENT.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INVALID_STUDENT.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnBadRequest_When_DeleteStudentIsFailed() throws Exception {
        doThrow(new StudentException("ERROR")).when(studentService).deleteStudent(STUDENT_ID);
        String url = STUDENT_BY_ID_URL.replace(REPLACE_STUDENT_ID, STUDENT_ID);
        mockMvc.perform(MockMvcRequestBuilders.delete(url)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorResponseStatus.INTERNAL_SERVER_ERROR.getStatusCode()))
                .andExpect(jsonPath("$.data", nullValue()));
    }


    /**
     * This method creates sample student
     *
     * @return Student
     */
    private Student getSampleStudent() {
        Student student = new Student();
        student.setStudentId(STUDENT_ID);
        student.setAddress(ADDRESS);
        student.setDeleted(false);
        student.setAge(AGE);
        student.setGrade(GRADE);
        student.setStudentStatus(StudentStatus.COMING);
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
        updateStudentRequestDto.setStudentStatus(StudentStatus.COMING.getStatus());
        updateStudentRequestDto.setPhoneNumber(PHONE_NUMBER);
        updateStudentRequestDto.setGender(GENDER);
        updateStudentRequestDto.setFirstName(UPDATED_FIRST_NAME);
        updateStudentRequestDto.setLastName(UPDATED_LAST_NAME);
        updateStudentRequestDto.setTuitionClassId(TUITION_CLASS_ID);
        return updateStudentRequestDto;
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

}