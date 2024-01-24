package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {
	public static Connection getConnection(String props)
	{
		Connection connection=null;
		String[] properties=props.split(" ");
		try {
			Class.forName(properties[2]);
			System.out.println("class loaded");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Class Not Established");
			e.printStackTrace();
		}
		
		try {
			connection = DriverManager.getConnection(  
					properties[3],properties[0],properties[1]);
			System.out.println("con established");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		return connection;
	}

}

