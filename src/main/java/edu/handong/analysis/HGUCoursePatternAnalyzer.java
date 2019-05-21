package edu.handong.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysise.utils.NotEnoughArgumentException;
import edu.handong.analysise.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		
		try {
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		String dataPath = args[0]; // csv file to be analyzed
		String resultPath = args[1]; // the file path where the results are saved.
		ArrayList<String> lines = Utils.getLines(dataPath, true);
		students = loadStudentCourseRecords(lines);
		
		// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
		
		// Generate result lines to be saved.
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
	/*
	System.out.println("num : " + sortedStudents.get("0001").getNumCourseInNthSemester(1));
	System.out.println("num : " + sortedStudents.get("0001").getNumCourseInNthSemester(2));
	System.out.println("num : " + sortedStudents.get("0001").getNumCourseInNthSemester(3));
	System.out.println("num : " + sortedStudents.get("0001").getNumCourseInNthSemester(4));*/
		// Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, resultPath);
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file.
	 * Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<String> lines) {
		HashMap<String,Student> hashmap = new HashMap<String,Student>();
		for(String s : lines)
		{
			Course cs = new Course(s);
			String key = cs.getStudentId();
			if(hashmap.containsKey(key))
			{
				Student stu = hashmap.get(key);
				stu.addCourse(cs);
			}
			else {
				Student stu = new Student(cs.getStudentId());
				hashmap.put(key,stu);
				stu.addCourse(cs);
			}
		}
		/*
		Set<String> keys = hashmap.keySet();
		//print all the keys
		for (String key : keys) {
			Student stu = hashmap.get(key);
			//System.out.println(stu.getStudentId());
			HashMap<String,Integer> hm = stu.getSemestersByYearAndSemester();
			for(Map.Entry<String,Integer> element : hm.entrySet()){
	            System.out.println("key : "+element.getKey()+" ,value : "+element.getValue());
			}
		}*/
		//한줄씩 받음 -> course class 만들기 -> hashmap에 key 있는지 확인 -> 없으면 student생성하 추가하고 addcourse , 있으면 그냥 addcourse
		// TODO: Implement this method
		
		return hashmap; // do not forget to return a proper variable.
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {

		// TODO: Implement this method
		ArrayList<String> courseinfo = new ArrayList<String>();
		//sortedStudents->student->semestersByYearAndSemester(value 1에서 부터  size까지)
		for (String tempkey : sortedStudents.keySet())
		{
			Student stu = sortedStudents.get(tempkey);
			HashMap<String,Integer> semesterByYearAndSemester = stu.getSemestersByYearAndSemester();
			for(int i = 0; i<stu.getTotalSemester(); i++)
			{
				for(Map.Entry<String,Integer> element : semesterByYearAndSemester.entrySet()){
					if(element.getValue()==i+1)
					{
						String str = stu.getStudentId() + ", " +stu.getTotalSemester()+ ", "
							+ element.getValue() + ", "+ stu.getNumCourseInNthSemester(element.getValue());
						courseinfo.add(str);
					}
				}
			}

		}
		return courseinfo; // do not forget to return a proper variable.
	}
}
