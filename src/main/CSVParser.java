package main;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
 
import com.opencsv.CSVReader;
 
public class CSVParser {
 
    public static void main(String[] args) {
        try{
         InputStreamReader is = new InputStreamReader(new FileInputStream("C:\\Users\\cow94\\Desktop\\demo2.csv"), "EUC-KR");
         CSVReader reader = new CSVReader(is);
         List<String[]> list = reader.readAll();
         
         for(String[] str : list){
             System.out.println();
             for(String s : str){
                 System.out.print(s + " ");
             }
         }
         
         
        }catch(Exception e){
            e.printStackTrace();
        }
    }
 
}
