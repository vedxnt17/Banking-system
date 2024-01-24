package util;

import java.sql.Connection;

public class DBUtil {
	

	public static Connection getDBConn()
	{
		Connection connection = null;
		String propstr=DBPropertyUtil.getPropertyString("../src/database.properties");
		connection=DBConnUtil.getConnection(propstr);
		return connection;
	}
}
