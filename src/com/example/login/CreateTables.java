package com.example.login;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This creates the tables to be used in the database.
 */
public class CreateTables {
    UserDatabase userDb = new UserDatabase();
    ItemsDatabase itemsDb = new ItemsDatabase();

    CreateTables(){
    }

    public void create(){
        try {
            DBConnection createSchema = new DBConnection();
            createSchema.create();
            Connection connection = createSchema.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS reviews");
            statement.execute("DROP TABLE IF EXISTS item_categories");
            statement.execute("DROP TABLE IF EXISTS items");
            statement.execute("DROP TABLE IF EXISTS categories");
            statement.execute("DROP TABLE IF EXISTS users");


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
    public void insertData(){
        userDb.insertUser("username", "password","Errol","Cattenach","ecattenach0@cmu.edu");
        userDb.insertUser("ecattenach0","rXIC75zfiC50","Errol","Cattenach","ecattenach0@cmu.edu");
        userDb.insertUser("fgearing1","LWeMY5eXonk","Flossie","Gearing","fgearing1@furl.net");
        userDb.insertUser("etommen2","xgObJl","Elli","Tommen","etommen2@istockphoto.com");
        userDb.insertUser("tantognetti4","rPivA0y3x6jT","Trina","Antognetti","tantognetti4@digg.com");

        itemsDb.insertItem("Textbook","Proin eu mi. Nulla ac enim. In tempor, turpis nec euismod scelerisque, quam turpis adipiscing lorem, vitae mattis nibh ligula nec sem. Duis aliquam convallis nunc. Proin at turpis a pede posuere nonummy.",5.44,"tantognetti4",new String[]{"Books"});
        itemsDb.insertItem("Macbook Pro","In eleifend quam a odio.",8.68,"etommen2",new String[]{"Computers"});
        itemsDb.insertItem("Volleyball","Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla dapibus dolor vel est. Donec odio justo, sollicitudin ut, suscipit a, feugiat et, eros. Vestibulum ac est lacinia nisi venenatis tristique. Fusce congue, diam id ornare imperdiet, sapien urna pretium nisl, ut volutpat sapien arcu sed augue.",7.46,"fgearing1",new String[]{"Sports"});
        itemsDb.insertItem("Windshield wipers","Lorem ipsum dolor sit amet, consectetuer adipiscing elit.",19.99,"fgearing1",new String[]{"Automotive"});
        itemsDb.insertItem("Set of plates","Lorem ipsum dolor sit amet, consectetuer adipiscing elit.",25.00,"tantognetti4",new String[]{"Home", "Decor"});

    }
}
