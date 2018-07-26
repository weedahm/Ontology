package main;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

	static final String TAG = "Main";

	public static void main(String[] args) {
		CSVParser csvParser = new CSVParser("C:\\Users\\cow94\\Desktop\\last.csv");
		ArrayList<String> parseData = new ArrayList<>();
		try {
			parseData = csvParser.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}