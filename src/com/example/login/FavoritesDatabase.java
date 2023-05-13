package com.example.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoritesDatabase {

    public void insertFavorite(String username, String favoriteUsername) {
        String query = "INSERT INTO favorites (username, favorite_user) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, favoriteUsername);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());  // Print the SQL exception message

        }

    }


    public List<String> getFavoriteUsers(String username) throws SQLException {
        List<String> favoriteUsers = new ArrayList<>();
        String query = "SELECT favorite_user FROM favorites WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String favoriteUser = rs.getString("favorite_user");
                favoriteUsers.add(favoriteUser);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return favoriteUsers;
    }

    public boolean isTableEmpty() throws SQLException {
        String query = "SELECT COUNT(*) FROM favorites";

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
}
