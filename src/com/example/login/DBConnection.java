package com.example.login;

import java.sql.*;

/**
 * This is the database connection, which allows users to
 * create and connect to database.
 * @author Erin Maldonado
 */
public class DBConnection {
	static String DB_URL = "jdbc:mysql://localhost:3306/";;
	static String DB_USER = "root";
	static String DB_PASS = "password";
	static String dbName = "ONLINE_STORE";

	Connection conn = null;
	Statement stmt = null;

	public DBConnection() {
	}

	/**
	 * Creates the database.
	 */
	public void create() {
		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			 Statement stmt = conn.createStatement()) {
			String sql = "CREATE DATABASE IF NOT EXISTS " + dbName;
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Connects to the database that has been created
	 * @return A Connection to the database.
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		//return the connection
		return DriverManager.getConnection(DB_URL + dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", DB_USER, DB_PASS);
	}
}
