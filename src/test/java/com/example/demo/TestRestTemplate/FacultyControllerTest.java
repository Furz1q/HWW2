package com.example.demo.TestRestTemplate;

import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.repository.FacultyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class FacultyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    void setUp() {
        facultyRepository.deleteAll();
    }

    @Test
    public void testCreateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculties", faculty, Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Gryffindor", response.getBody().getName());
    }

    @Test
    public void testGetFacultyById() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculties/1", Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateFaculty() {
        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setName("Slytherin");
        updatedFaculty.setColor("Green");

        restTemplate.put("/faculties/1", updatedFaculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculties/1", Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Slytherin", response.getBody().getName());
    }

    @Test
    public void testDeleteFaculty() {
        restTemplate.delete("/faculties/1");

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculties/1", Faculty.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetStudentsByFaculty() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity("/faculties/1/students", Student[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void testGetFacultyById_NotFound() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculties/999", Faculty.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
