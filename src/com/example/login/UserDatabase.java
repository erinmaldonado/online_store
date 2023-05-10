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

	public List<Item> getUsersByTwoCategoriesSameDay(String categoryX, String categoryY) {
		List<Item> userItems = new ArrayList<>();
		ItemsDatabase itemsDatabase = new ItemsDatabase();

		String query = "SELECT i1.item_id, i1.username, i1.item_name AS itemName, i1.item_description AS itemDescription, i1.item_price AS itemPrice, c1.category_name AS categoryName, DATE(i1.date_posted) AS datePosted " +
				"FROM items i1 " +
				"JOIN item_categories ic1 ON i1.item_id = ic1.item_id " +
				"JOIN categories c1 ON ic1.category_id = c1.category_id " +
				"JOIN items i2 ON i1.username = i2.username AND DATE(i1.date_posted) = DATE(i2.date_posted) " +
				"JOIN item_categories ic2 ON i2.item_id = ic2.item_id " +
				"JOIN categories c2 ON ic2.category_id = c2.category_id " +
				"WHERE c1.category_name = ? AND c2.category_name = ? AND i1.item_id <> i2.item_id " +
				"GROUP BY i1.item_id, i1.username, i1.item_name, i1.item_description, i1.item_price, c1.category_name, DATE(i1.date_posted)";

		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, categoryX);
			preparedStatement.setString(2, categoryY);

			ResultSet resultSet = preparedStatement.executeQuery();
			itemsDatabase.setItemInfo(userItems, resultSet);
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userItems;
	}
}
