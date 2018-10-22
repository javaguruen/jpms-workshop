package com.jpmsworkshop.students.api;

public class Student {
	private Long id;
	private String name;
	private String studentNumber;

	public Student() {
		super();
	}

	public Student(Long id, String name, String studentNumber) {
		super();
		this.id = id;
		this.name = name;
		this.studentNumber = studentNumber;
	}

	public Student(String name, String studentNumber) {
		super();
		this.name = name;
		this.studentNumber = studentNumber;
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

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	@Override
	public String toString() {
		return String.format("Student [id=%s, name=%s, studentNumber=%s]", id, name, studentNumber);
	}

}
