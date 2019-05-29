package edu.handong.analysis.datamodel;

import org.apache.commons.csv.CSVRecord;

public class Course {
	
	private String studentId;
	private String yearMonthGraduated;
	private String firstMajor;
	private String secondMajor;
	private String courseCode;
	private String courseName;
	private String courseCredit;
	private int yearTaken;
	private int semesterCourseTaken;
	
	public Course(CSVRecord csvRecord)
	{
		studentId = csvRecord.get(0);
		yearMonthGraduated = csvRecord.get(1);
		firstMajor = csvRecord.get(2);
		secondMajor = csvRecord.get(3);
		courseCode = csvRecord.get(4);
		courseName = csvRecord.get(5);
		courseCredit = csvRecord.get(6);
		yearTaken = Integer.parseInt(csvRecord.get(7));
		semesterCourseTaken = Integer.parseInt(csvRecord.get(8));
	}

	public String getTimeCourseTaken() {
		return yearTaken + "-" + semesterCourseTaken;
	}
	public String getStudentId()
	{
		return studentId;
	}

	public String getCourseCode() {
		return courseCode;
	}
	public String getCourseName() {
		return courseName;
	}

	public int getYearTaken() {
		return yearTaken;
	}

	public int getSemesterCourseTaken() {
		return semesterCourseTaken;
	}

}
