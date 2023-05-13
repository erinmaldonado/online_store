package com.example.login;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * This creates the tables to be used in the database.
 */
public class CreateTables {
    UserDatabase userDatabase = new UserDatabase();
    ItemsDatabase itemsDatabase = new ItemsDatabase();
    CategoriesDatabase categoriesDatabase = new CategoriesDatabase();
    ReviewsDatabase reviewsDatabase = new ReviewsDatabase();
    FavoritesDatabase favoritesDatabase = new FavoritesDatabase();

    // Add categories
    String[] categoryNames = new String[]{"Electronics", "Gadgets", "Accessories", "Books", "Fiction", "Non-Fiction", "Automotive", "Home", "Garden", "Tires"};

    // Dummy data for reviews
    String[] scores = {"Excellent", "Good", "Fair", "Poor"};

    // Add users
    List<User> usersList = new ArrayList<>();

    // Dummy data for remarks
    String[] remarks = new String[10];

    int itemId = -1;

    CreateTables() {
    }

    public void dropTables(Statement statement) throws SQLException {
        statement.execute("DROP TABLE IF EXISTS favorites");
        statement.execute("DROP TABLE IF EXISTS reviews");
        statement.execute("DROP TABLE IF EXISTS item_categories");
        statement.execute("DROP TABLE IF EXISTS items");
        statement.execute("DROP TABLE IF EXISTS categories");
        statement.execute("DROP TABLE IF EXISTS users");
    }

    public void create() {
        try {
            DBConnection createSchema = new DBConnection();
            createSchema.create();
            Connection connection = createSchema.getConnection();
            Statement statement = connection.createStatement();

            dropTables(statement);

            /**
             * Table for storing user information.
             */
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "username varchar(99) NOT NULL,"
                    + "password varchar(99) NOT NULL,"
                    + "firstName varchar(20) NOT NULL,"
                    + "lastName varchar(20) NOT NULL,"
                    + "email varchar(99) NOT NULL,"
                    + "PRIMARY KEY (username),"
                    + "UNIQUE KEY email (email)"
                    + ")";
            statement.executeUpdate(createUsersTable);
            /**
             * Table for storing category information.
             */
            String createCategoriesTable = "CREATE TABLE IF NOT EXISTS categories ("
                    + "category_id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "category_name VARCHAR(255) UNIQUE NOT NULL"
                    + ")";
            statement.executeUpdate(createCategoriesTable);

            /**
             * Table for storing item information.
             */
            String createItemsTable = "CREATE TABLE IF NOT EXISTS items ("
                    + "item_id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "item_name VARCHAR(255) NOT NULL,"
                    + "item_description TEXT NOT NULL,"
                    + "date_posted DATE NOT NULL,"
                    + "item_price DECIMAL(10, 2) NOT NULL,"
                    + "username varchar(20) NOT NULL,"
                    + "FOREIGN KEY (username) REFERENCES users(username)"
                    + ")";


            statement.executeUpdate(createItemsTable);

            /**
             * Table for storing item category information.
             */
            String createItemCategoriesTable = "CREATE TABLE IF NOT EXISTS item_categories ("
                    + "item_id INT NOT NULL,"
                    + "category_id INT NOT NULL,"
                    + "PRIMARY KEY (item_id, category_id),"
                    + "FOREIGN KEY (item_id) REFERENCES items(item_id),"
                    + "FOREIGN KEY (category_id) REFERENCES categories(category_id)"
                    + ")";
            statement.executeUpdate(createItemCategoriesTable);

            /**
             * Table for storing item reviews.
             */
            String createReviewsTable = "CREATE TABLE IF NOT EXISTS reviews ("
                    + "review_id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "item_id INT NOT NULL,"
                    + "username varchar(20) NOT NULL,"
                    + "review_date DATE NOT NULL,"
                    + "score ENUM('Excellent', 'Good', 'Fair', 'Poor') NOT NULL,"
                    + "remark TEXT,"
                    + "UNIQUE (item_id, username),"
                    + "FOREIGN KEY (item_id) REFERENCES items(item_id),"
                    + "FOREIGN KEY (username) REFERENCES users(username)"
                    + ")";

            statement.executeUpdate(createReviewsTable);

            String createFavoritesTable = "CREATE TABLE IF NOT EXISTS favorites ("
                    + "username VARCHAR(20) NOT NULL,"
                    + "favorite_user VARCHAR(20) NOT NULL,"
                    + "FOREIGN KEY (username) REFERENCES users(username),"
                    + "FOREIGN KEY (favorite_user) REFERENCES users(username),"
                    + "PRIMARY KEY (username, favorite_user)"
                    + ")";

            statement.executeUpdate(createFavoritesTable);


            insertData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getRandom(String[] arr) {
        int rand = new Random().nextInt(arr.length);
        return rand;
    }

    public int getRandomFromArray(List<User> list) {
        Random rand = new Random();
        return rand.nextInt(list.size());
    }


    /**
     * Dummy data for testing purposes
     */
    public void insertData() throws SQLException {
        if(userDatabase.isTableEmpty()){
            for (int i = 0; i < 50; i++) {
                User user = new User(("username" + i), ("password" + i), ("firstName" + i), ("lastName" + i), ("email" + i + "@gmail.com"));
                usersList.add(user);
                userDatabase.insertUser(user);
            }
        }

        if(categoriesDatabase.isTableEmpty()){
            // Insert category names into database
            for (String categoryName : categoryNames) {
                categoriesDatabase.insertCategory(categoryName);
            }

        }

        // Current date
        Date datePosted = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        for (int i = 0; i < 10; i++) {
            remarks[i] = "I rate this item a " + (i + 1) + " out of 10";
        }

        if(itemsDatabase.isTableEmpty()){
            // Insert items into database
            for (int i = 0; i < 60; i++) {
                int random = getRandomFromArray(usersList);
                User user = usersList.get(random);
                String username = user.getUsername();

                String itemName = "item: " + i;
                String itemDescription = "description: " + i;
                Random r = new Random();

                double randomPrice = 0.99 + (1999.99 - .99) * r.nextDouble();
                double itemPrice = randomPrice;

                itemId = itemsDatabase.insertItem(itemName, itemDescription, datePosted, itemPrice, username);

                Item item = new Item(itemId, itemName, itemDescription, datePosted, itemPrice, username);
                user.addUserItem(item);

                int j = getRandom(categoryNames);
                int k = getRandom(categoryNames);

                if (j != k) {
                    itemsDatabase.insertItemCategories(itemId, new String[]{categoryNames[j], categoryNames[k]});
                } else {
                    itemsDatabase.insertItemCategories(itemId, new String[]{categoryNames[j]});
                }
            }
        }

        if(reviewsDatabase.isTableEmpty()){
            // Insert reviews into database
            for (int i = 0; i < usersList.size(); i++) {
                User user = usersList.get(i);
                String username = user.getUsername();
                List<Item> items = user.getItemList();

                for (int j = 0; j < items.size(); j++) {
                    Item item = items.get(j);
                    itemId = item.getItemId();

                    int l;
                    User reviewer;
                    String reviewerUsername;

                    int totalReviews = reviewsDatabase.getTotalReviews(itemId);
                    while (totalReviews < 6) {
                        do {
                            l = getRandomFromArray(usersList);
                            reviewer = usersList.get(l);
                            reviewerUsername = reviewer.getUsername();
                        } while (username.equals(reviewerUsername) || reviewsDatabase.userReviewedItem(reviewerUsername, itemId));

                        int m = getRandom(remarks);

                        if (j == 0 && i < usersList.size()/2) { // Ensure first item for each user has only "Excellent" or "Good" reviews.
                            if (m >= 4 && m <= 6) { // between 5 and 6
                                l = 1; // Good
                            } else if (m > 6 && m <= 9) { // between 7 and 9
                                l = 0; // Excellent
                            }
                        } else {
                            if (m >= 0 && m <= 2) { // between 0 and 2
                                l = 3; // Poor
                            } else if (m > 2 && m <= 4) { // between 3 and 4
                                l = 2; // Fair
                            } else if (m > 4 && m <= 6) { // between 5 and 6
                                l = 1; // Good
                            } else if (m > 6 && m <= 9) { // between 7 and 9
                                l = 0; // Excellent
                            }
                        }

                        if (l >= 0 && l < scores.length && m >= 0 && m < remarks.length) { // Ensure we have a valid index for scores and remarks arrays
                            reviewsDatabase.insertReview(itemId, reviewerUsername, scores[l], remarks[m]);
                            totalReviews = reviewsDatabase.getTotalReviews(itemId);
                        }
                    }
                }
            }
        }

        if(favoritesDatabase.isTableEmpty()){
            for (int i = 0; i < usersList.size(); i++) {
                int count = 0;
                User user = usersList.get(i);
                String username = user.getUsername();
                List<User> favoriteUsers = new ArrayList<>();

                while (count < 5) {
                    int random = getRandomFromArray(usersList);
                    User favoriteUser = usersList.get(random);
                    if (!favoriteUsers.contains(favoriteUser) && !user.equals(favoriteUser)) {
                        favoriteUsers.add(favoriteUser);
                        favoritesDatabase.insertFavorite(username, favoriteUser.getUsername());
                        count++;
                    }
                }
                user.setUserFavorites(favoriteUsers);
            }
        }
    }
}