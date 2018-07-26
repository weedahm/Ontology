package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSVParser {
	String csvFilePath;
	String line = "";
	String csvSplitBy = ",";
	
	CSVParser(String csvFilePath) {
		this.csvFilePath = csvFilePath;
	}
	
	public ArrayList<String> parse() throws IOException {
		FileInputStream fileInputStream = new FileInputStream(csvFilePath);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "MS949");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		
		ArrayList<String> result = new ArrayList<String>();
		
		while ((line = bufferedReader.readLine()) != null) {
			result.add(line);
			System.out.println(line);
        }
		
		bufferedReader.close();
		
		return result;
	}
}