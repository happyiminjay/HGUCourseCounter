package edu.handong.analysis.datamodel;

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
	
	public Course(String line)
	{
		String[] strlist = line.split(",");
		studentId = strlist[0].trim();
		yearMonthGraduated = strlist[1].trim();
		firstMajor = strlist[2].trim();
		secondMajor = strlist[3].trim();
		courseCode = strlist[4].trim();
		courseName = strlist[5].trim();
		courseCredit = strlist[6].trim();
		yearTaken = Integer.parseInt(strlist[7].trim());
		semesterCourseTaken = Integer.parseInt(strlist[8].trim());
		/*System.out.println(studentId+" "+yearMonthGraduated+" "+firstMajor+" "+secondMajor+" "+courseCode+" "+courseName
				+" "+courseCredit+" "+yearTaken+" "+semesterCourseTaken);*/
	}

	public String getTimeCourseTaken() {
		return yearTaken + "-" + semesterCourseTaken;
	}
	public String getStudentId()
	{
		return studentId;
	}
}
