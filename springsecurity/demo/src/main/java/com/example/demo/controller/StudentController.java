package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private StudentService service;
    
    // GET 방식으로 학생 목록 요청
    @GetMapping("/list")
    public List<Student> getAllStudents() {
        return service.list();
    }

    // POST endpoint to add a new student
    @PostMapping("/save")
    public void addStudent(@RequestBody Student student) {
        service.add(student);
    }
}