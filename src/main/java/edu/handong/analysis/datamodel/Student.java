package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Student {
	private String studentId;
	private ArrayList<Course> coursesTaken = new ArrayList<Course>();
	private HashMap<String,Integer> semestersByYearAndSemester =  new HashMap<String,Integer>();
	public Student(String studentId)
	{
		this.studentId = studentId;
	}
	public void addCourse(Course newRecord)
	{
		coursesTaken.add(newRecord);
	}
	public HashMap<String,Integer> getSemestersByYearAndSemester()
	{
		TreeMap<String,Integer> treemap = new TreeMap<>();
		for(Course a : coursesTaken)
		{
			String s = a.getTimeCourseTaken();
			treemap.put(s,0);
		}
        Set<Map.Entry<String,Integer>> entries = treemap.entrySet();
        int i = 1;
        for(Map.Entry<String, Integer> tempEntry: entries){
        	if(!semestersByYearAndSemester.containsKey(tempEntry.getKey()))
        	semestersByYearAndSemester.put(tempEntry.getKey(),i);
        	i++; 
        }

		return semestersByYearAndSemester;
	}
	public int getNumCourseInNthSemester(int semester)
	{
		int count = 0;
		String key = null;
		for (String tempkey : semestersByYearAndSemester.keySet())
		{
			if(semestersByYearAndSemester.get(tempkey)==semester)
			{
				key = tempkey;
				break;
			}
		}
		for(Course cu : coursesTaken)
		{
			if(cu.getTimeCourseTaken().equals(key)) count++;
		}
		// semester을 입력 -> key값 알아내기-> coursesTaken for문안에 course에 getTimeCourseTaken()로 string compare -> count++;
		return count;
	}
	public String getStudentId()
	{
		return studentId;
	}
	public int getTotalSemester()
	{
		return semestersByYearAndSemester.size();
	}

}
