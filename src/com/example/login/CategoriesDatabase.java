package com.example.login;

import java.sql.*;

public class CategoriesDatabase {
    public  CategoriesDatabase(){
    }

    public int insertCategory(String categoryName) {
        int categoryId = getCategoryIdByName(categoryName);

        if (categoryId == -1) {
            String query = "INSERT INTO categories (name) VALUES (?)";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, categoryName);

                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    categoryId = generatedKeys.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return categoryId;
    }

    public int getCategoryIdByName(String categoryName) {
        String query = "SELECT category_id FROM categories WHERE name = ?";
        int categoryId = -1;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, categoryName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                categoryId = resultSet.getInt("category_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryId;
    }

    public int getCategoryId(int itemId) {
        int categoryId = -1;
        String query = "SELECT category_id FROM item_categories WHERE item_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, itemId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                categoryId = resultSet.getInt("category_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryId;
    }

    public String getCategoryName(int categoryId) {
        String itemName = "";
        String query = "SELECT name FROM categories WHERE  category_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                itemName = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemName;
    }


}
