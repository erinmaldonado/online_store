package com.example.login;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
	public boolean insertUser(User user){
		try (Connection conn = DBConnection.getConnection()) {
			String checkQuery = "SELECT COUNT(*) FROM users WHERE email=?";
			PreparedStatement preparedStatement = conn.prepareStatement(checkQuery);
			preparedStatement.setString(1, user.getEmail());

			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int count = resultSet.getInt(1);
			if (count > 0) {
				return false;
			} else {
				String query = "INSERT INTO users (username, password, firstName, lastName, email) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement insertStmt = conn.prepareStatement(query);
				insertStmt.setString(1, user.getUsername());
				insertStmt.setString(2, user.getPassword());
				insertStmt.setString(3, user.getFirstName());
				insertStmt.setString(4, user.getLastName());
				insertStmt.setString(5, user.getEmail());
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

	public void loadUserItems(User user) {
		try (Connection conn = DBConnection.getConnection()) {
			String query = "SELECT * FROM items WHERE username = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, user.getUsername());

			ResultSet resultSet = preparedStatement.executeQuery();

			List<Item> items = new ArrayList<>();
			while (resultSet.next()) {
				Item item = new Item();
				item.setItemId(resultSet.getInt("item_id"));
				item.setItemName(resultSet.getString("item_name"));
				item.setItemDescription(resultSet.getString("item_description"));
				item.setItemPrice(resultSet.getDouble("item_price"));
				item.setDatePosted(resultSet.getDate("date_posted"));
				items.add(item);
			}
			user.setUserItems(items);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
