package com.example.login;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
	public boolean insertUser(String username, String password, String firstName, String lastName, String email){
		try (Connection conn = DBConnection.getConnection()) {
			String checkQuery = "SELECT COUNT(*) FROM users WHERE email=?";
			PreparedStatement preparedStatement = conn.prepareStatement(checkQuery);
			preparedStatement.setString(1, email);

			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int count = resultSet.getInt(1);
			if (count > 0) {
				return false;
			} else {
				String query = "INSERT INTO users (username, password, firstName, lastName, email) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement insertStmt = conn.prepareStatement(query);
					System.out.println("username: " + username);
					insertStmt.setString(1, username);
					insertStmt.setString(2, password);
					insertStmt.setString(3, firstName);
					insertStmt.setString(4, lastName);
					insertStmt.setString(5, email);
					insertStmt.executeUpdate();
					resultSet.close();
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
				resultSet.close();
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<UserItems> getUsersByTwoCategoriesSameDay(String categoryX, String categoryY) {
		List<UserItems> userItemInfos = new ArrayList<>();
		String query = "SELECT i1.username, i1.categoryName, i1.itemName, DATE(i1.postingDate) as postingDate " +
				"FROM items i1 " +
				"JOIN items i2 ON i1.username = i2.username AND DATE(i1.postingDate) = DATE(i2.postingDate) " +
				"WHERE i1.categoryName = ? AND i2.categoryName = ? " +
				"AND i1.itemId <> i2.itemId " +
				"GROUP BY i1.username, i1.categoryName, i1.itemName, DATE(i1.postingDate)";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, categoryX);
			preparedStatement.setString(2, categoryY);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				UserItems userItemInfo = new UserItems(resultSet.getString("username"),
						resultSet.getString("categoryName"),
						resultSet.getString("itemName"),
						resultSet.getDate("postingDate").toLocalDate());
				userItemInfos.add(userItemInfo);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userItemInfos;
	}

	public boolean usernameExists(String username) {
		String query = "SELECT COUNT(*) FROM users WHERE username = ?";
		int count = 0;

		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count > 0;
	}
}
