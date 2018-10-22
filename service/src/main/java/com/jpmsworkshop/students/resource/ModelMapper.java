package com.jpmsworkshop.students.resource;

import java.util.List;

import com.jpmsworkshop.students.dao.Student;

import static java.util.stream.Collectors.toList;

class ModelMapper {

  private ModelMapper() {
  }

  static List<com.jpmsworkshop.students.Student> map2api(List<Student> daoStudents) {
    return daoStudents.stream()
        .map(ModelMapper::map2api)
        .collect(toList());
  }

  static com.jpmsworkshop.students.Student map2api(Student daoStudent) {
    return new com.jpmsworkshop.students.Student(daoStudent.getId(), daoStudent.getName(), daoStudent.getStudentNumber());
  }

  static Student map2Model(com.jpmsworkshop.students.Student student) {
    return new com.jpmsworkshop.students.dao.Student(student.getId(), student.getName(), student.getStudentNumber());
  }
}
