package com.example.demo.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student> students;

    public Faculty() {}

    public Faculty(Long l, String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", students=" + (students != null ? students.size() : "null") +
                '}';
    }
}
