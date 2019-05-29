package edu.handong.analysise.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Utils {
	public static ArrayList<CSVRecord> getLines(String file,int startyear, int endyear)//int startyear, int endyear
	{
		ArrayList<CSVRecord> arraylist = new ArrayList<CSVRecord>();
		try {
			
			BufferedReader reader = Files.newBufferedReader(Paths.get(file));
			@SuppressWarnings("resource")
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.
	        					withHeader("StudentID","YearMonthGraduated","FistMajor","SecondMajor",
	        					"CourseCode","CourseName","CourseCredit","YearTaken","SemesterTaken").
	        					withIgnoreHeaderCase().withTrim()); 
			int i = 0;
			for (CSVRecord csvRecord: csvParser) {
	            // Accessing Values by Column Index
				if(i==0)
					{
						i++;
						continue;
					}
				if(Integer.parseInt(csvRecord.get(7))<startyear||Integer.parseInt(csvRecord.get(7))>endyear)
				continue; //**********************************************************************
		
				arraylist.add(csvRecord);
	        }
		}
		catch (NoSuchFileException e) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return arraylist;	
	}
	public static void writeAFile(ArrayList<String> lines, String targetFileName)
	{
		 try (FileWriter writer = new FileWriter(targetFileName)){
			 writer.write("StudentID,TotalNumberOfSemestersRegistered,"
			 		+ "Semester,NumCoursesTakenInTheSemester\n");
		        for (String str : lines) {
		           writer.write(str+"\n");
			    }
		        writer.flush();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}
	public static void writeAFile2(ArrayList<String> lines, String targetFileName)
	{
		 try (FileWriter writer = new FileWriter(targetFileName)){
			 writer.write("Year,Semester,CouseCode, CourseName,TotalStudents,StudentsTaken,Rate\n");
		        for (String str : lines) {
		           writer.write(str+"\n");
			    }
		        writer.flush();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}
}
