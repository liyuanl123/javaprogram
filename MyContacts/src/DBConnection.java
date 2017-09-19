/*
 * Configuration with MySQL database, connection with database, create table
 * */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	static private DBConnection instance = null;
	private Connection conn = null;

	public Connection getConnection() {
		return conn;
	}

	private DBConnection() {
		// use mysql
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/MyContacts?autoReconnect=true&useSSL=false";

		String user = "root";
		String pwd = "19870212";

		try {
			Class.forName(driver).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(url, user, pwd);
			conn.setAutoCommit(true);

			// create table first
			String sqlCreate = "create table IF NOT EXISTS Contacts (id integer AUTO_INCREMENT, name varchar(10), birthday date, telephone varchar(30), email varchar(30), remark varchar(100),PRIMARY KEY(id))";
			conn.createStatement().execute(sqlCreate);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
	}

	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new DBConnection();
		}

		return instance;
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
	}
}
