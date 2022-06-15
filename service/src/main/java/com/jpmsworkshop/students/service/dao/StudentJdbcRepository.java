package com.jpmsworkshop.students.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StudentJdbcRepository {
	JdbcTemplate jdbcTemplate;

	@Autowired
	public StudentJdbcRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	class StudentRowMapper implements RowMapper<Student> {
		@Override
		public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
			Student student = new Student();
			student.setId(rs.getLong("id"));
			student.setName(rs.getString("name"));
			student.setStudentNumber(rs.getString("student_number"));
			return student;
		}

	}

	public List<Student> findAll() {
		return jdbcTemplate.query("select * from student", new StudentRowMapper());
	}

	public Optional<Student> findById(long id) {
		return Optional.ofNullable(jdbcTemplate.queryForObject("select * from student where id=?", new Object[] { id },
				new BeanPropertyRowMapper<Student>(Student.class)));
	}

	public int deleteById(long id) {
		return jdbcTemplate.update("delete from student where id=?", new Object[] { id });
	}

	public int insert(Student student) {
		return jdbcTemplate.update("insert into student (id, name, student_number) " + "values(?,  ?, ?)",
				new Object[] { student.getId(), student.getName(), student.getStudentNumber() });
	}

	public int update(Student student) {
		return jdbcTemplate.update("update student " + " set name = ?, student_number = ? " + " where id = ?",
				new Object[] { student.getName(), student.getStudentNumber(), student.getId() });
	}

}
