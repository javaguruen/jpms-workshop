package com.jpmsworkshop.students.service;

import java.util.List;
import java.util.Optional;

import com.jpmsworkshop.students.dao.Student;

public interface StudentService {

  List<Student> findAll();

  Optional<Student> findById(long id);

  void deleteById(long id);

  int insert(Student student);

  int update(Student student);
}
