package com.example.login;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
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
     */
    public int insertItem(String title, String description, double price, String username) {
        String query = "INSERT INTO items (title, description, date_posted, price, username) VALUES (?, ?, ?, ?, ?)";

        int itemId = -1;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setDate(3, date);
            preparedStatement.setDouble(4, price);
            preparedStatement.setString(5, username);

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                itemId = generatedKeys.getInt(1);
            }
            generatedKeys.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemId;
    }

    public void insertItemCategories(int itemId, String[] categoryNames) {
        String query = "INSERT INTO item_categories (item_id, category_id) VALUES (?, ?)";
        CategoriesDatabase categoriesDatabase = new CategoriesDatabase();

        for (String categoryName : categoryNames) {
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                int categoryId = categoriesDatabase.insertCategory(categoryName);

                System.out.println("Category name: " + categoryName);
                System.out.println("Category ID: " + categoryId);

                preparedStatement.setInt(1, itemId);
                preparedStatement.setInt(2, categoryId);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        String query = "SELECT items.*, categories.category_id, categories.name AS category_name " +
                "FROM items " +
                "JOIN item_categories ON items.item_id = item_categories.item_id " +
                "JOIN categories ON item_categories.category_id = categories.category_id " +
                "WHERE (categories.category_id, items.price) IN " +
                "(SELECT item_categories.category_id, MAX(items.price) FROM items JOIN item_categories ON items.item_id = item_categories.item_id GROUP BY item_categories.category_id) " +
                "ORDER BY categories.name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Item item = new Item();
                item.setItemId(resultSet.getInt("item_id"));
                item.setItemName(resultSet.getString("title"));
                item.setItemDescription(resultSet.getString("description"));
                item.setItemPrice(resultSet.getDouble("price"));
                item.setCategoryId(resultSet.getInt("category_id"));
                item.setUsername(resultSet.getString("username"));

                mostExpensiveItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mostExpensiveItems;
    }

    private void getItems(List<Item> itemsList, ResultSet rs) throws SQLException {
        getItemInfo(itemsList, rs);
    }

    private void getItemInfo(List<Item> itemsList, ResultSet rs) throws SQLException {
        while (rs.next()) {
            int itemId = rs.getInt("item_id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            Date datePosted = rs.getDate("date_posted");
            double price = rs.getDouble("price");
            String username = rs.getString("username");


            Item item = new Item(itemId, title, description, datePosted, price, username);
            itemsList.add(item);
        }
    }
}
