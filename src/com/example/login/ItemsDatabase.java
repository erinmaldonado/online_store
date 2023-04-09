package com.example.login;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ItemsDatabase {
    public void insertItem(String title, String description, double price, String username, String[] categoryNames) {
        String query = "INSERT INTO items (title, description, price, username, date_posted) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, price);
            preparedStatement.setString(4, username);
            preparedStatement.setDate(5, new java.sql.Date(System.currentTimeMillis()));

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int itemId = rs.getInt(1);

                for (String categoryName : categoryNames) {
                    int categoryId = getCategoryId(categoryName.trim());
                    insertItemCategory(itemId, categoryId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getCategoryId(String categoryName) {
        String query = "SELECT category_id FROM categories WHERE name = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, categoryName);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("category_id");
            } else {
                return insertCategory(categoryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int insertCategory(String categoryName) {
        String query = "INSERT INTO categories (name) VALUES (?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, categoryName);

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void insertItemCategory(int itemId, int categoryId) {
        String query = "INSERT INTO item_categories (item_id, category_id) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, itemId);
            preparedStatement.setInt(2, categoryId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getItemsPostedToday(String username) {
        String query = "SELECT COUNT(*) FROM items WHERE username = ? AND DATE(date_posted) = CURDATE()";
        int itemCount = 0;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                itemCount = resultSet.getInt(1);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemCount;
    }

    public List<Item> searchItemsByCategory(String category) {
        List<Item> items = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM items WHERE category=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                String itemDescription = rs.getString("item_description");
                double itemPrice = rs.getDouble("item_price");
                String itemCategory = rs.getString("category");
                Item item = new Item(itemId, itemName, itemDescription, itemPrice, itemCategory);
                items.add(item);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return items;
    }
}
