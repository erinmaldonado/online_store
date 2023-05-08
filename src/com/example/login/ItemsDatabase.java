package com.example.login;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Erin Maldonado
 */
public class ItemsDatabase {
    public ItemsDatabase(){}

    /**
     * @param title
     * @param description
     * @param price
     * @param username
     * @param categoryNames
     */
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


    /**
     * @param categoryName
     * @return
     */
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

    /**
     * @param categoryName
     * @return
     */
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

    /**
     * @param itemId
     * @param categoryId
     */
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


    /**
     * @param username
     * @return
     */
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

    /**
     * @param category
     * @return
     */
    public List<Item> searchItemsByCategory(String category) {
        List<Item> items = new ArrayList<>();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection conn = null;
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT items.*, categories.name FROM items " +
                    "JOIN item_categories ON items.item_id = item_categories.item_id " +
                    "JOIN categories ON item_categories.category_id = categories.category_id " +
                    "WHERE categories.name = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, category);
            resultSet = preparedStatement.executeQuery();

            // Extract the data from the ResultSet
            getResults(items, resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return items;
    }


    /**
     * @param items
     * @param resultSet
     * @throws SQLException
     */
    private void getResults(List<Item> items, ResultSet resultSet) throws SQLException {
        getItems(items, resultSet);
    }


    /**
     * @return
     */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM items")) {

            try (ResultSet resultSet = stmt.executeQuery()) {
                getResults(items, resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }


    public List<Item> getMostExpensiveItemsInEachCategory() {
        List<Item> mostExpensiveItems = new ArrayList<>();
        String query = "WITH ranked_items AS ( " +
                "SELECT i.item_id, i.title, i.description, i.price, i.username, i.date_posted, c.name, " +
                "       ROW_NUMBER() OVER (PARTITION BY c.category_id ORDER BY i.price DESC) AS row_num " +
                "FROM items i " +
                "JOIN item_categories ic ON i.item_id = ic.item_id " +
                "JOIN categories c ON c.category_id = ic.category_id " +
                ") " +
                "SELECT item_id, title, description, price, username, date_posted, name " +
                "FROM ranked_items " +
                "WHERE row_num = 1;";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                Date datePosted = rs.getDate("date_posted");
                double price = rs.getDouble("price");
                String username = rs.getString("username");
                String category = rs.getString("name");

                Item item = new Item(itemId, title, description, datePosted, price, username, category);
                mostExpensiveItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mostExpensiveItems;
    }

    public String getCategory(int categoryId) {
        String query = "SELECT name FROM categories WHERE category_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, categoryId);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void getItems(List<Item> itemsList, ResultSet rs) throws SQLException {
        while (rs.next()) {
            int itemId = rs.getInt("item_id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            Date datePosted = rs.getDate("date_posted");
            double price = rs.getDouble("price");
            String username = rs.getString("username");
            String category = getCategory(itemId); // Get the category from the ResultSet

            Item item = new Item(itemId, title, description, datePosted, price, username, category);
            itemsList.add(item);
        }
    }

    public List<String> getUsersWithItemsInCategoriesOnSameDay(String categoryX, String categoryY) {
        List<String> users = new ArrayList<>();
        String query = "SELECT i1.username " +
                "FROM items i1 " +
                "JOIN item_categories ic1 ON i1.item_id = ic1.item_id " +
                "JOIN categories c1 ON ic1.category_id = c1.category_id " +
                "JOIN items i2 ON i1.username = i2.username AND DATE(i1.date_posted) = DATE(i2.date_posted) " +
                "JOIN item_categories ic2 ON i2.item_id = ic2.item_id " +
                "JOIN categories c2 ON ic2.category_id = c2.category_id " +
                "WHERE c1.name = ? AND c2.name = ? AND i1.item_id <> i2.item_id " +
                "GROUP BY i1.username;";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, categoryX);
            preparedStatement.setString(2, categoryY);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

}
