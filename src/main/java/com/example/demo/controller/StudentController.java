package com.example.demo.controller;

import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/age-between")
    public List<Student> getStudentsByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.getStudentsByAgeBetween(min, max);
    }
    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(value -> ResponseEntity.ok(value.getFaculty()))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student savedStudent = studentService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        boolean deleted = studentService.deleteStudent(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
