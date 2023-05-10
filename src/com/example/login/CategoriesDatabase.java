package com.example.login;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDatabase {
    public  CategoriesDatabase(){
    }

    public int insertCategory(String categoryName) {
        int categoryId = getCategoryIdByName(categoryName);

        if (categoryId == -1) {
            String query = "INSERT INTO categories (category_name) VALUES (?)";

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
        String query = "SELECT category_id FROM categories WHERE category_name = ?";
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

    public String getCategoryNameWithId(int categoryId) {
        String categoryName = "";
        String query = "SELECT category_name FROM categories WHERE  category_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                categoryName = resultSet.getString("category_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryName;
    }

    public List<String> getCategoryNamesWithItemId(int itemId){
        List<String> categories = new ArrayList<>();

        String query = "SELECT c.category_name " +
                "FROM categories c " +
                "JOIN item_categories ic ON c.category_id = ic.category_id " +
                "WHERE ic.item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, itemId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categories.add(resultSet.getString("category_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public String getCategoryNameWithItemId(int itemId){
        String categoryName = "";

        String query = "SELECT c.category_name " +
                "FROM categories c " +
                "JOIN item_categories ic ON c.category_id = ic.category_id " +
                "WHERE ic.item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, itemId);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.getString(categoryName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryName;
    }
}
