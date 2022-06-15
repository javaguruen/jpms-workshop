package com.jpmsworkshop.students.service.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jpmsworkshop.students.service.dao.Student;
import com.jpmsworkshop.students.service.exceptions.StudentNotFoundException;
import com.jpmsworkshop.students.service.StudentService;

@RestController
public class StudentResource {

  private StudentService studentService;

  @Autowired
  public StudentResource(StudentService studentService) {
    this.studentService = studentService;
  }

  @GetMapping("/students")
  public List<com.jpmsworkshop.students.api.Student> retrieveAllStudents() {
    return ModelMapper.map2api(studentService.findAll());
  }

  @GetMapping("/students/{id}")
  public com.jpmsworkshop.students.api.Student retrieveStudent(@PathVariable long id) {
    Optional<com.jpmsworkshop.students.service.dao.Student> student = studentService.findById(id);
    if (!student.isPresent()) {
      throw new StudentNotFoundException("id-" + id);
    }

    return ModelMapper.map2api(student.get());
  }

  @DeleteMapping("/students/{id}")
  public void deleteStudent(@PathVariable long id) {
    studentService.deleteById(id);
  }

  @PostMapping("/students")
  public ResponseEntity<Object> createStudent(@RequestBody com.jpmsworkshop.students.api.Student student) {
    int numUpdated = studentService.insert( ModelMapper.map2Model(student));

    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(student.getId()).toUri();

    return ResponseEntity.created(location).build();
  }

  @PutMapping("/students/{id}")
  public ResponseEntity<Object> updateStudent(@RequestBody Student student, @PathVariable long id) {

    Optional<com.jpmsworkshop.students.service.dao.Student> studentOptional = studentService.findById(id);
    if (!studentOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    com.jpmsworkshop.students.service.dao.Student daoStudent = studentOptional.get();
    daoStudent.setName(student.getName());
    daoStudent.setStudentNumber(student.getStudentNumber());

    studentService.update(daoStudent);

    return ResponseEntity.noContent().build();
  }
}
