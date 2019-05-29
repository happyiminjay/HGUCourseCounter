package edu.handong.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVRecord;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysise.utils.NotEnoughArgumentException;
import edu.handong.analysise.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	String inputPath;
	String outputPath;
	String analysisOption;
	String courseCode;
	int total = 0;//**********************************************************************
	int startYear;
	int endYear;
	boolean help;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		Options options = createOptions();
		
		if(parseOptions(options,args)) {
			if(help) {
				printHelp(options);
				return;
			}
			//-o data/results.csv -i data/hw5data.csv -a 1 -s 2003 -e 2007
			if(analysisOption.equals("2"))
			{
				if(courseCode==null) 
				{
					printHelp(options);
					return;
				}
			}
			ArrayList<CSVRecord> lines = Utils.getLines(inputPath,startYear,endYear);
			students = loadStudentCourseRecords(lines);

			// Generate result lines to be saved.
			if(analysisOption.equals("1"))
			{
				// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
				Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
				// Write a file (named like the value of resultPath) with linesTobeSaved.
				Utils.writeAFile(linesToBeSaved, outputPath);

			}
			else {
				ArrayList<String> linesToBeSaved = new ArrayList<String>();
				//2008,1,ECE20016,자바프로그래밍언어,30,10,33.3%
				String courseName = null;
				String rate;
				String addInfo;
				float totalStudent, studentsTaken;
				for(int year = startYear; year<=endYear; year++)
				{
					for(int semesterCourseTaken = 1; semesterCourseTaken<=4; semesterCourseTaken++)
					{
						totalStudent = 0;
						studentsTaken = 0;
						for(String key : students.keySet())
						{
							Student stu = students.get(key);
							ArrayList<Course> coursesTaken = stu.getCourseTaken();
							int i = 0;
							for(Course course : coursesTaken)
							{
								if(course.getSemesterCourseTaken()==semesterCourseTaken&&course.getYearTaken()==year)
								{
									i++;
									if(course.getCourseCode().equals(courseCode))
									{
										studentsTaken ++;
										courseName = course.getCourseName();
									}
								}
							}
							if(i!=0) totalStudent ++;
						}
						if(studentsTaken == 0) continue;
						rate = String.format("%.1f%%",studentsTaken/totalStudent*100);
						addInfo = year + "," + semesterCourseTaken + "," + courseCode + "," + courseName + ","
										+ (int)totalStudent + "," + (int)studentsTaken + "," + rate;
						total += studentsTaken;
						linesToBeSaved.add(addInfo);
					}
				}
				Utils.writeAFile2(linesToBeSaved, outputPath);
			}
		}
	}

	/**
	 * This method create HashMap<String,Student> from the data csv file.
	 * Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<CSVRecord> csvrecords) {
		HashMap<String,Student> hashmap = new HashMap<String,Student>();
		for(CSVRecord csvrecord : csvrecords)
		{
			Course cs = new Course(csvrecord);
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

		//한줄씩 받음 -> course class 만들기 -> hashmap에 key 있는지 확인 -> 없으면 student생성하 추가하고 addcourse , 있으면 그냥 addcourse
		
		return hashmap; 
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
		return courseinfo; 
	}
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);

			inputPath = cmd.getOptionValue("i");
			outputPath = cmd.getOptionValue("o");
			analysisOption = cmd.getOptionValue("a");
			courseCode = cmd.getOptionValue("c");
			startYear = Integer.parseInt(cmd.getOptionValue("s"));
			endYear = Integer.parseInt(cmd.getOptionValue("e"));
			help = cmd.hasOption("h");
			
		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
		
	}
	
	private Options createOptions() {
		Options options = new Options();

		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());

		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("Output path")
				.required() 
				.build());
		
		options.addOption(Option.builder("a").longOpt("analysis")
		        .desc("1: Count courses per semester, 2: Count per course name and year")
		        .hasArg()
		        .argName("Analysis option")
		        .required()
		        .build());
		
		options.addOption(Option.builder("c").longOpt("coursecode")
				.desc("Course code for '-a 2' option")
				.hasArg()
				.argName("course code")
				//.required()
				.build());
		//**********************************Yes only for 'a 2'**********************************************
		options.addOption(Option.builder("s").longOpt("startyear")
				.desc("Set the start year for analysis e.g., -s 2002")
				.hasArg()
				.argName("Start year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("e").longOpt("endyear")
				.desc("Set the end year for analysis e.g., -e 2005")
				.hasArg()
				.argName("End year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("h").longOpt("help")
				.argName("Help")
		        .desc("Show a Help page")
		        .build());
		
		return options;
	}
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer ="";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}
}
