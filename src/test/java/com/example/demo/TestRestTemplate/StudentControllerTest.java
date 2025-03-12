package com.example.demo.TestRestTemplate;

import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class StudentControllerTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8080/students";

    private Faculty faculty;

    @BeforeEach
    public void setup() {
        studentRepository.deleteAll();
        faculty = new Faculty(1L, "Gryffindor", "Red");

        Student student = new Student(null, "Harry Potter", 17, faculty);
        studentRepository.save(student);
    }

    @Test
    public void testGetStudentsByAgeBetween() {
        int minAge = 16;
        int maxAge = 18;

        ResponseEntity<List> response = restTemplate.exchange(
                baseUrl + "/age-between?min=" + minAge + "&max=" + maxAge,
                HttpMethod.GET,
                null,
                List.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isGreaterThan(0);
    }

    @Test
    public void testGetStudentFaculty() {
        Long studentId = 1L;
        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl + "/" + studentId + "/faculty",
                HttpMethod.GET,
                null,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
    }

    @Test
    public void testGetStudentById() {
        Long studentId = 1L;

        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl + "/" + studentId,
                HttpMethod.GET,
                null,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter");
    }

    @Test
    public void testCreateStudent() {
        Student newStudent = new Student(null, "Hermione Granger", 18, faculty);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> request = new HttpEntity<>(newStudent, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.POST,
                request,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Hermione Granger");
    }

    @Test
    public void testDeleteStudent() {
        Long studentId = 1L;

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + studentId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void testDeleteStudent_NotFound() {
        Long studentId = 100L;

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + studentId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
