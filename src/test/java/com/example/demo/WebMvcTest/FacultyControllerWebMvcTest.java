package com.example.demo.WebMvcTest;

import com.example.demo.controller.FacultyController;
import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.service.FacultyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Test
    public void testSearchFaculties() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");

        when(facultyService.searchFaculties("Gryffindor", "Red")).thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculties/search")
                        .param("name", "Gryffindor")
                        .param("color", "Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[0].color").value("Red"));
    }

    @Test
    public void testGetStudentsByFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        List<Student> students = List.of(
                new Student(1L, "Harry Potter", 17, faculty),
                new Student(2L, "Ron Weasley", 18, faculty)
        );
        faculty.setStudents(students);

        when(facultyService.getFacultyById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculties/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[1].name").value("Ron Weasley"));
    }


    @Test
    public void testGetFacultyById() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");

        when(facultyService.getFacultyById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    public void testGetFacultyById_NotFound() throws Exception {
        when(facultyService.getFacultyById(100L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/faculties/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty(null, "Hufflepuff", "Yellow");
        Faculty savedFaculty = new Faculty(2L, "Hufflepuff", "Yellow");

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(savedFaculty);

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Hufflepuff\", \"color\": \"Yellow\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Hufflepuff"));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        doNothing().when(facultyService).deleteFaculty(1L);

        mockMvc.perform(delete("/faculties/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFaculty_NotFound() throws Exception {
        when(facultyService.deleteFaculty(100L)).thenReturn(false);

        mockMvc.perform(delete("/faculties/100"))
                .andExpect(status().isNotFound());
    }
}
