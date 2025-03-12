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
    void testGetFacultyById_Success() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");

        Mockito.when(facultyService.getFacultyById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"));
    }

    @Test
    void testGetFacultyById_NotFound() throws Exception {
        Mockito.when(facultyService.getFacultyById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isNotFound());
    }
}
