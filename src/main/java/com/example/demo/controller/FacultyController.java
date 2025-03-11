package com.example.demo.controller;

import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faculties")
public class FacultyController {

    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @GetMapping("/search")
    public List<Faculty> searchFaculties(@RequestParam String name, @RequestParam String color) {
        return facultyService.searchFaculties(name, color);
    }
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudentsByFaculty(@PathVariable Long id) {
        Optional<Faculty> faculty = facultyService.getFacultyById(id);
        return faculty.map(value -> ResponseEntity.ok(value.getStudents()))
                .orElse(ResponseEntity.notFound().build());
    }
}
