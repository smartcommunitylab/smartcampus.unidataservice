package eu.trentorise.smartcampus.unidataservice.model;

import java.util.ArrayList;
import java.util.List;

import smartcampus.service.esse3.data.message.Esse3.Exam;
import smartcampus.service.esse3.data.message.Esse3.StudentExams;

public class StudentInfoExams {

	private String idAda;
	private List<StudentInfoExam> exams;
	
	public StudentInfoExams(StudentExams studentExams) {
		this.idAda = studentExams.getIdAda();
		exams = new ArrayList<StudentInfoExam>();
		for (Exam exam: studentExams.getExamsList()) {
			exams.add(new StudentInfoExam(exam));
		}
	}

	public String getIdAda() {
		return idAda;
	}

	public void setIdAda(String idAda) {
		this.idAda = idAda;
	}

	public List<StudentInfoExam> getExams() {
		return exams;
	}

	public void setExams(List<StudentInfoExam> exams) {
		this.exams = exams;
	}
	
	
	
}
