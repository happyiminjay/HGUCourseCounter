package edu.handong.analysise.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {
	public static ArrayList<String> getLines(String file, boolean removeHeader)
	{
		ArrayList<String> arraylist = new ArrayList<String>();
		try {
			File csv = new File(file);
			BufferedReader br = new BufferedReader(new FileReader(csv));
			String line;
			if(removeHeader) {
				int i = 0;
				while ((line = br.readLine()) != null)
				{
					if(i==0) 
						{	
							i=1;
							continue;
						}
					//System.out.println(line);
					arraylist.add(line);
				}
				br.close();
			}
			else {
				while ((line = br.readLine()) != null)
				{
					arraylist.add(line);
				}
				br.close();
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		/*int stop = 0;
		for(String str : arraylist)
		{
			stop++;
			if(stop==100) break;
			System.out.println(str);
		}*/
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
}
