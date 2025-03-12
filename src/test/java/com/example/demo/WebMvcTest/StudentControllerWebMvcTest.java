package com.example.demo.WebMvcTest;

import com.example.demo.controller.StudentController;
import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    public void testGetStudentsByAgeBetween() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        Student student1 = new Student(1L, "Harry Potter", 17, faculty);
        Student student2 = new Student(2L, "Ron Weasley", 18, faculty);

        when(studentService.getStudentsByAgeBetween(16, 18)).thenReturn(List.of(student1, student2));

        mockMvc.perform(get("/students/age-between")
                        .param("min", "16")
                        .param("max", "18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[1].name").value("Ron Weasley"));
    }

    @Test
    public void testGetStudentFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        Student student = new Student(1L, "Harry Potter", 17, faculty);

        when(studentService.getStudentById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"));
    }

    @Test
    public void testGetStudentFaculty_NotFound() throws Exception {
        when(studentService.getStudentById(100L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/100/faculty"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetStudentById() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        Student student = new Student(1L, "Harry Potter", 17, faculty);

        when(studentService.getStudentById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    public void testGetStudentById_NotFound() throws Exception {
        when(studentService.getStudentById(100L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateStudent() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        Student student = new Student(null, "Hermione Granger", 18, faculty);
        Student savedStudent = new Student(2L, "Hermione Granger", 18, faculty);

        when(studentService.createStudent(any(Student.class))).thenReturn(savedStudent);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Hermione Granger\", \"age\": 18, \"faculty\": {\"id\": 1}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Hermione Granger"));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteStudent_NotFound() throws Exception {
        when(studentService.deleteStudent(100L)).thenReturn(false);

        mockMvc.perform(delete("/students/100"))
                .andExpect(status().isNotFound());
    }
}
