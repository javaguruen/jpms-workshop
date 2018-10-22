package com.jpmsworkshop.students.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpmsworkshop.students.dao.Student;
import com.jpmsworkshop.students.dao.StudentJdbcRepository;

@Service
public class StudentServiceImpl implements StudentService{

  private StudentJdbcRepository studentRepository;

  @Autowired
  public StudentServiceImpl(StudentJdbcRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public List<Student> findAll(){
    return studentRepository.findAll();
  }

  public Optional<Student> findById(long id){
    return studentRepository.findById(id);
  }

  public void deleteById(long id){
    studentRepository.deleteById(id);
  }

  public int insert(Student student){
    return studentRepository.insert(student);
  }

  @Override
  public int update(Student student) {
    return studentRepository.update(student);
  }
}

