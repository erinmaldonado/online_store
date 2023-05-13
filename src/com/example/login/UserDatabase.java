package com.example.login;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

	public List<Item> getUserItems(String username) {
		List<Item> items = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection()) {
			String query = "SELECT * FROM items WHERE username = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, username);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Item item = new Item();
				item.setItemId(resultSet.getInt("item_id"));
				item.setItemName(resultSet.getString("item_name"));
				item.setItemDescription(resultSet.getString("item_description"));
				item.setItemPrice(resultSet.getDouble("item_price"));
				item.setDatePosted(resultSet.getDate("date_posted"));
				items.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public List<User> getTopPostersSince(Date date) throws SQLException {
		List<User> topPosters = new ArrayList<>();

		String query = "SELECT username, COUNT(item_id) as item_count FROM items WHERE date_posted >= ? GROUP BY username ORDER BY item_count DESC";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setDate(1, date);

			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					User topPoster = new User(rs.getString("username"));
					List<Item> userItems = getUserItems(topPoster.getUsername());
					topPoster.setUserItems(userItems);
					topPosters.add(topPoster);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return topPosters;
		}
	}

	public boolean isTableEmpty() throws SQLException {
		String query = "SELECT COUNT(*) FROM users";

		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			ResultSet rs = preparedStatement.executeQuery(query);

			if (rs.next()) {
				int count = rs.getInt(1);
				return count == 0;
			} else {
				return true;
			}
		}
	}

	public List<User> getUsernames() {
		List<User> users = new ArrayList<>();

		String query = "SELECT * FROM users";

		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String username = resultSet.getString("username");
				User user = new User(username);
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}


	public List<String> getUsersWithLessThanThreeExcellentReviews() {
		List<String> usernames = new ArrayList<>();
		String query = "SELECT i.username FROM items i LEFT JOIN reviews r ON i.item_id = r.item_id GROUP BY i.username HAVING SUM(CASE WHEN r.score = 'Excellent' THEN 1 ELSE 0 END) < 3";

		try (Connection connection = DBConnection.getConnection();
			 Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				usernames.add(resultSet.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return usernames;
	}


	public List<String> getUsersWhoNeverPostedPoorReview() {
		List<String> usernames = new ArrayList<>();
		String query = "SELECT DISTINCT username FROM reviews WHERE username NOT IN (SELECT username FROM reviews WHERE score = 'Poor')";

		try (Connection connection = DBConnection.getConnection();
			 Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				usernames.add(resultSet.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return usernames;
	}

	public List<String> getUsersWhoOnlyPostedPoorReviews() {
		List<String> usernames = new ArrayList<>();
		String query = "SELECT DISTINCT username FROM reviews WHERE username NOT IN (SELECT DISTINCT username FROM reviews WHERE score <> 'Poor')";

		try (Connection connection = DBConnection.getConnection();
			 Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				usernames.add(resultSet.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return usernames;
	}

	public List<String> getUsersWithNoPoorReviews() {
		List<String> usernames = new ArrayList<>();
		String query = "SELECT DISTINCT users.username FROM users JOIN items ON users.username = items.username WHERE items.item_id NOT IN (SELECT DISTINCT reviews.item_id FROM reviews WHERE reviews.score = 'Poor')";

		try (Connection connection = DBConnection.getConnection();
			 Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				usernames.add(resultSet.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usernames;
	}

	public void getUserPairsWithExcellentReviews() {
		String query = "SELECT A.username AS userA, B.username AS userB " +
				"FROM users AS A " +
				"JOIN reviews AS reviewA ON A.username = reviewA.username " +
				"JOIN items AS itemA ON reviewA.item_id = itemA.item_id AND itemA.username != A.username " +
				"JOIN users AS B ON itemA.username = B.username " +
				"JOIN items AS itemB ON B.username = itemB.username " +
				"JOIN reviews AS reviewB ON itemB.item_id = reviewB.item_id AND reviewB.username = A.username " +
				"WHERE reviewA.score = 'Excellent' AND reviewB.score = 'Excellent' " +
				"GROUP BY A.username, B.username " +
				"HAVING COUNT(reviewA.item_id) = (SELECT COUNT(*) FROM items WHERE username = B.username) " +
				"AND COUNT(reviewB.item_id) = (SELECT COUNT(*) FROM items WHERE username = A.username)";


		DefaultTableModel model = new DefaultTableModel(new String[]{"User A", "User B"}, 0);

		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String userA = resultSet.getString("userA");
				String userB = resultSet.getString("userB");

				model.addRow(new Object[]{userA, userB});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Create the table
		JTable table = new JTable(model);

		// Create scroll pane (for large data sets)
		JScrollPane pane = new JScrollPane(table);

		// Create frame
		JFrame frame = new JFrame("User Pairs with Excellent Reviews");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(pane);
		frame.pack();
		frame.setVisible(true);
	}
}
