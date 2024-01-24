package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
	public static String getPropertyString(String file_name)
	{
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file_name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(file_name+" File is missing");
		}
		Properties p1=new Properties();
		try {
			p1.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String propstr=p1.getProperty("username")+" "+p1.getProperty("password")+" "+p1.getProperty("driver")+" "+p1.getProperty("url");
		return propstr;
		
	}

}
