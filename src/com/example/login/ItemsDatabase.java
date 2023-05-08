package com.example.login;

import java.sql.*;
import java.util.ArrayList;
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
            String sql = "SELECT items.* FROM items " +
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
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT category FROM items")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(rs.getString("category"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
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
        String query = "SELECT i.item_id, i.title, i.description, i.price, i.username, c.name " +
                "FROM items i " +
                "JOIN item_categories ic ON i.item_id = ic.item_id " +
                "JOIN categories c ON c.category_id = ic.category_id " +
                "WHERE (i.item_id, ic.category_id) IN ( " +
                "    SELECT item_id, category_id " +
                "    FROM items i " +
                "    JOIN item_categories ic ON i.item_id = ic.item_id " +
                "    WHERE (price, category_id) IN ( " +
                "        SELECT MAX(price), category_id " +
                "        FROM items i " +
                "        JOIN item_categories ic ON i.item_id = ic.item_id " +
                "        GROUP BY category_id " +
                "    ) " +
                ")";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            getItems(mostExpensiveItems, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mostExpensiveItems;
    }

    private void getItems(List<Item> mostExpensiveItems, ResultSet rs) throws SQLException {
        while (rs.next()) {
            int itemId = rs.getInt("item_id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String datePosted = rs.getString("date_posted");
            double price = rs.getDouble("price");
            String username = rs.getString("username");

            Item item = new Item(itemId, title, description, datePosted, price, username);
            mostExpensiveItems.add(item);
        }
    }
}
