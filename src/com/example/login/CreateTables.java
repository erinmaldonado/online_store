package com.example.login;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

/**
 * This creates the tables to be used in the database.
 */
public class CreateTables {
    UserDatabase userDatabase = new UserDatabase();
    ItemsDatabase itemsDatabase = new ItemsDatabase();
    CategoriesDatabase categoriesDatabase = new CategoriesDatabase();

    CreateTables(){
    }

    public void dropTables(Statement statement) throws SQLException {
        statement.execute("DROP TABLE IF EXISTS reviews");
        statement.execute("DROP TABLE IF EXISTS item_categories");
        statement.execute("DROP TABLE IF EXISTS items");
        statement.execute("DROP TABLE IF EXISTS categories");
        statement.execute("DROP TABLE IF EXISTS users");
    }

    public void create(){
        try {
            DBConnection createSchema = new DBConnection();
            createSchema.create();
            Connection connection = createSchema.getConnection();
            Statement statement = connection.createStatement();

            dropTables(statement);

            /**
             * Table for storing user information.
             */
            String createUsersTable = "CREATE TABLE users ("
                    + "username varchar(20) NOT NULL,"
                    + "password varchar(20) NOT NULL,"
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
            String createCategoriesTable = "CREATE TABLE categories ("
                    + "category_id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255) UNIQUE NOT NULL"
                    + ")";
            statement.executeUpdate(createCategoriesTable);

            /**
             * Table for storing item information.
             */
            String createItemsTable = "CREATE TABLE items ("
                    + "item_id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "title VARCHAR(255) NOT NULL,"
                    + "description TEXT NOT NULL,"
                    + "date_posted DATE NOT NULL,"
                    + "price DECIMAL(10, 2) NOT NULL,"
                    + "username varchar(20) NOT NULL,"
                    + "FOREIGN KEY (username) REFERENCES users(username)"
                    + ")";


            statement.executeUpdate(createItemsTable);

            /**
             * Table for storing item category information.
             */
            String createItemCategoriesTable = "CREATE TABLE item_categories ("
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
            String createReviewsTable = "CREATE TABLE reviews ("
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

            System.out.println("Tables created successfully.");
            insertData();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * dummy data to insert
     */
    public void insertData() {
        String username1 = "username";
        userDatabase.insertUser(username1, "password", "Erin", "Maldonado", "username@username.edu");

        String username2 = "ecattenach0";
        userDatabase.insertUser(username2, "rXIC75zfiC50", "Errol", "Cattenach", "ecattenach0@cmu.edu");

        String username3 = "fgearing1";
        userDatabase.insertUser(username3, "LWeMY5eXonk", "Flossie", "Gearing", "fgearing1@furl.net");

        String username4 = "etommen2";
        userDatabase.insertUser(username4, "xgObJl", "Elli", "Tommen", "etommen2@istockphoto.com");


        String[] categoryNames = new String[]{"Electronics", "Gadgets", "Accessories", "Books", "Fiction", "Non-Fiction", "Automotive", "Home", "Garden"};
        for (String categoryName : categoryNames) {
            categoriesDatabase.insertCategory(categoryName);
        }

        // insert item 1
        int itemId1 = itemsDatabase.insertItem("Item 1", "Description 1", 10.99, username1);
        System.out.println("itemid: " + itemId1 + "/n");
        itemsDatabase.insertItemCategories(itemId1, new String[]{"Electronics", "Gadgets"});

        // insert item 2
        int itemId2 = itemsDatabase.insertItem("Item 2", "Description 2", 15.99, username2);
        System.out.println("itemid2: " + itemId2 + "/n");
        itemsDatabase.insertItemCategories(itemId2, new String[]{"Electronics", "Accessories"});

        // insert item 3
        int itemId3 = itemsDatabase.insertItem("Item 3", "Description 3", 20.99, username3);
        System.out.println("itemid3: " + itemId3 + "/n");
        itemsDatabase.insertItemCategories(itemId3, new String[]{"Books", "Fiction"});


        // insert item 4
        int itemId4 = itemsDatabase.insertItem("Item 4", "Description 4", 25.99, username4);
        System.out.println("itemid4: " + itemId4 + "/n");
        itemsDatabase.insertItemCategories(itemId4, new String[]{"Books", "Non-Fiction"});


        // insert item 5
        int itemId5 = itemsDatabase.insertItem("Item 5", "Description 5", 50.99, username4);
        System.out.println("itemid5: " + itemId5);
        itemsDatabase.insertItemCategories(itemId5, new String[]{"Automotive", "Accessories"});

        //insert item 6
        int itemId6 = itemsDatabase.insertItem("Item 6", "Description 6", 1999.99, username4);
        System.out.println("itemid6: " + itemId6);
        itemsDatabase.insertItemCategories(itemId5, new String[]{"Home", "Garden"});
    }
}
