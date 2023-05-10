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
     * @param itemName
     * @param itemDescription
     * @param itemPrice
     * @param username
     */
    public int insertItem(String itemName, String itemDescription, double itemPrice, String username) {
        String query = "INSERT INTO items (item_name, item_description, date_posted, item_price, username) VALUES (?, ?, ?, ?, ?)";

        int itemId = -1;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            java.sql.Date datePosted = new java.sql.Date(Calendar.getInstance().getTime().getTime());

            preparedStatement.setString(1, itemName);
            preparedStatement.setString(2, itemDescription);
            preparedStatement.setDate(3, datePosted);
            preparedStatement.setDouble(4, itemPrice);
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
            String sql = "SELECT items.*, categories.category_name FROM items " +
                    "JOIN item_categories ON items.item_id = item_categories.item_id " +
                    "JOIN categories ON item_categories.category_id = categories.category_id " +
                    "WHERE categories.category_name = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, category);
            resultSet = preparedStatement.executeQuery();

            getItemInfo(items, resultSet);
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
     * @return
     */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT items.item_id, items.item_name AS itemName, items.item_description AS itemDescription, items.item_price AS itemPrice, items.date_posted AS datePosted, items.username AS username from items")) {

            try (ResultSet resultSet = stmt.executeQuery()) {
                getItemInfo(items, resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    private List<Item> getItemInfo(List<Item> itemsList, ResultSet rs) throws SQLException {
        while (rs.next()) {
            int itemId = rs.getInt("item_id");
            String itemName = rs.getString("itemName");
            String itemDescription = rs.getString("itemDescription");
            Date datePosted = rs.getDate("datePosted");
            double price = rs.getDouble("itemPrice");
            String username = rs.getString("username");

            Item item = new Item(itemId, itemName, itemDescription, datePosted, price, username);
            CategoriesDatabase categoriesDatabase = new CategoriesDatabase();
            List<String> categories = categoriesDatabase.getCategoryNamesWithItemId(itemId);
            item.setCategoryNames(categories);
            itemsList.add(item);
        }
        return itemsList;
    }

    static void setItemInfo(List<Item> userItems, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Item item = new Item();
            item.setItemId(resultSet.getInt("item_id"));
            item.setItemName(resultSet.getString("itemName"));
            item.setItemDescription(resultSet.getString("itemDescription"));
            item.setDatePosted(resultSet.getDate("datePosted"));
            item.setItemPrice(resultSet.getDouble("itemPrice"));
            item.setUsername(resultSet.getString("username"));
            item.setCategoryName(resultSet.getString("categoryName"));
            userItems.add(item);
        }
    }

    public List<Item> getMostExpensiveItemsInEachCategory() {
        List<Item> mostExpensiveItems = new ArrayList<>();
        String query = "SELECT items.item_id, items.item_name AS itemName, items.item_description AS itemDescription, items.item_price AS itemPrice, items.date_posted AS datePosted, items.username AS username, categories.category_id, categories.category_name AS categoryName " +
                "FROM items " +
                "JOIN item_categories ON items.item_id = item_categories.item_id " +
                "JOIN categories ON item_categories.category_id = categories.category_id " +
                "WHERE (categories.category_id, items.item_price) IN " +
                "(SELECT item_categories.category_id, MAX(items.item_price) FROM items JOIN item_categories ON items.item_id = item_categories.item_id GROUP BY item_categories.category_id) " +
                "ORDER BY categories.category_name";


        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            setItemInfo(mostExpensiveItems, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mostExpensiveItems;
    }
}
