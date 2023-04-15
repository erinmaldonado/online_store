package com.example.login;
import java.sql.*;

public class UserDatabase {
	public boolean insertUser(String username, String password, String firstName, String lastName, String email){
		try (Connection conn = DBConnection.getConnection()) {
			String checkQuery = "SELECT COUNT(*) FROM users WHERE email=?";
			PreparedStatement preparedStatement = conn.prepareStatement(checkQuery);
			preparedStatement.setString(1, email);
			ResultSet checkResult = preparedStatement.executeQuery();
			checkResult.next();
			int count = checkResult.getInt(1);
			if (count > 0) {
				// User with the same email already exists in the database
				return false;
			} else {
				String query = "INSERT INTO users (username, password, firstName, lastName, email) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement insertStmt = conn.prepareStatement(query);
					insertStmt.setString(1, username);
					insertStmt.setString(2, password);
					insertStmt.setString(3, firstName);
					insertStmt.setString(4, lastName);
					insertStmt.setString(5, email);
					insertStmt.executeUpdate();
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean registerUser(User user) {
		String query = "INSERT INTO users (username, password, firstName, lastName, email) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DBConnection.getConnection();
			  PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getFirstName());
			preparedStatement.setString(4, user.getLastName());
			preparedStatement.setString(5, user.getEmail());

			int rowsAffected = preparedStatement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public User loginUser(String username, String password) {
		String query = "SELECT * FROM users WHERE username = ? AND password = ?";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				User user = new User(resultSet.getString("username"), resultSet.getString("password"),
						resultSet.getString("firstName"), resultSet.getString("lastName"),
						resultSet.getString("email"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
