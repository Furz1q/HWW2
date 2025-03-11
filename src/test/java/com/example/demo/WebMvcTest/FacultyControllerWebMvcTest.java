package com.example.demo.WebMvcTest;

import com.example.demo.controller.FacultyController;
import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.service.FacultyService;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FacultyController.class)
@RunWith(SpringRunner.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Test
    public void testGetStudentsByFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        Student student1 = new Student(1L, "Harry", 17, faculty);
        Student student2 = new Student(2L, "Ron", 17, faculty);

        faculty.setStudents(List.of(student1, student2));
        Mockito.when(facultyService.getFacultyById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculties/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Harry"))
                .andExpect(jsonPath("$[1].name").value("Ron"));
    }
}
