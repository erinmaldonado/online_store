package com.example.login;

import java.sql.*;
import java.time.LocalDate;

public class ReviewsDatabase {

    public void insertReview(int itemId, String username, String score, String remark) throws SQLException {
        String query = "INSERT INTO reviews (item_id, username, review_date, score, remark) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, itemId);
            preparedStatement.setString(2, username);
            preparedStatement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            preparedStatement.setString(4, score);
            preparedStatement.setString(5, remark);
            preparedStatement.executeUpdate();
        }
    }

    public boolean userReviewedItem(String username, int itemId) throws SQLException {
        String query = "SELECT * FROM reviews WHERE username = ? AND item_id = ?";
        ResultSet resultSet;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Set the prepared statement parameters
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, itemId);

            // Execute the prepared statement
            resultSet = preparedStatement.executeQuery();

            // If a result is found, the user has already reviewed the item
            return resultSet.next();
        }
    }

    public boolean hasPostedThreeReviews(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM reviews WHERE username = ? AND review_date = CURDATE()";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count >= 3;
            } else {
                return false;
            }
        }
    }
}
