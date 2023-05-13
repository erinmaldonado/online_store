package com.example.login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class UserOptions {
    public JPanel userOptionsPanel;
    private JButton postButton;
    private JButton buyButton;
    private JButton reviewButton;
    private JButton initializeButton;
    private JButton mostExpensiveInCategoryButton;
    private JButton usersTwoItemsButton;
    private JButton userXExcellentButton;
    private JButton usersWithMostButton;
    private JButton usersFavoritedByButton;
    private JButton usersWithoutAnyButton;
    private JButton allUsersWhoButton;
    private JButton allUserReviewsButton;
    private JButton userHasNeverButton;
    private JButton userPairAButton;

    private final User currentUser;

    public UserOptions(User user) {
        this.currentUser = user;

        postButton.addActionListener(e -> {
            JFrame postItemFrame = new PostItemFrame(currentUser);
            postItemFrame.setVisible(true);
            SwingUtilities.getWindowAncestor(postButton).dispose();
        });

        reviewButton.addActionListener(e->{
            JFrame reviewItemFrame = new ReviewItemFrame(currentUser);
            reviewItemFrame.setVisible(true);
            SwingUtilities.getWindowAncestor(reviewButton).dispose();
        });

        initializeButton.addActionListener(e->{
            ItemsDatabase itemsDb = new ItemsDatabase();
            List<Item> items = itemsDb.getAllItems();

            JFrame searchResultFrame = new JFrame("Search Results");
            searchResultFrame.setSize(500, 500);
            searchResultFrame.setLocationRelativeTo(null);
            String[] columnNames = {"ID", "Name", "Description", "Price", "Categories", "Username", "Date Posted"};
            Object[][] data = new Object[items.size()][7];
            if (!items.isEmpty()) {
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);
                    data[i][0] = item.getItemId();
                    data[i][1] = item.getItemName();
                    data[i][2] = item.getItemDescription();
                    data[i][3] = "$" + item.getItemPrice();
                    data[i][4] = item.getCategoryNames();
                    data[i][5] = item.getUsername();
                    data[i][6] = item.getDatePosted();
                }
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            searchResultFrame.getContentPane().add(scrollPane);
            searchResultFrame.setVisible(true);
        });

        mostExpensiveInCategoryButton.addActionListener(e -> {
            ItemsDatabase itemsDb = new ItemsDatabase();
            List<Item> items = itemsDb.getMostExpensiveItemsInEachCategory();

            JFrame searchResultFrame = new JFrame("Most Expensive Items in Each Category");
            searchResultFrame.setSize(800, 500);
            searchResultFrame.setLocationRelativeTo(null);

            String[] columnNames = {"Category", "ID", "Name", "Description", "Username", "Date Posted", "Price"};
            Object[][] data = new Object[items.size()][7];
            if (!items.isEmpty()) {
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);

                    data[i][0] = item.getCategoryName();
                    data[i][1] = item.getItemId();
                    data[i][2] = item.getItemName();
                    data[i][3] = item.getItemDescription();
                    data[i][4] = item.getUsername();
                    data[i][5] = "$" + item.getItemPrice();
                    data[i][6] = item.getDatePosted();
                }
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            searchResultFrame.getContentPane().add(scrollPane);
            searchResultFrame.setVisible(true);
        });

        usersTwoItemsButton.addActionListener(e->{
            JFrame enterUserFrame = new EnterCategoriesFrame();
            enterUserFrame.setVisible(true);
            SwingUtilities.getWindowAncestor(usersTwoItemsButton).dispose();
        });


        userXExcellentButton.addActionListener(e->{
            JFrame excellentGoodReviewsFrame = new SearchExcellentGoodReviewsFrame();
            excellentGoodReviewsFrame.setVisible(true);
            SwingUtilities.getWindowAncestor(userXExcellentButton).dispose();
        });

        usersWithMostButton.addActionListener(e -> {
            UserDatabase userDb = new UserDatabase();
            Date date = new java.sql.Date(05/01/2020);

            List<User> users = null;

            try {
                users = userDb.getTopPostersSince(date);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            JFrame searchResultFrame = new JFrame("Top Posters Since 5/1/2020");
            searchResultFrame.setSize(800, 500);
            searchResultFrame.setLocationRelativeTo(null);

            String[] columnNames = {"Username", "Number of Items Posted"};
            Object[][] data = new Object[users.size()][2];
            if (!users.isEmpty()) {
                for (int i = 0; i < users.size(); i++) {
                    User topPoster = users.get(i);

                    data[i][0] = topPoster.getUsername();
                    data[i][1] = topPoster.getNumberOfItemsPosted();
                }
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            searchResultFrame.getContentPane().add(scrollPane);
            searchResultFrame.setVisible(true);
        });

        usersFavoritedByButton.addActionListener(e->{
            JFrame frame = new JFrame("Select users X and Y to get common favorites");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            UserDatabase userDatabase = new UserDatabase();

            // Create a JPanel
            JPanel panel = new JPanel();

            List<User> allUsers = userDatabase.getUsernames();
            String[] usernames = new String[allUsers.size()];

            for(int i = 0; i < allUsers.size(); i++){
                usernames[i] = allUsers.get(i).getUsername();
            }

            JComboBox<String> dropdown1 = new JComboBox<>(usernames);
            JComboBox<String> dropdown2 = new JComboBox<>(usernames);

            // Create a JButton for search
            JButton searchButton = new JButton("Search");
            searchButton.addActionListener(e1 -> {
                String userX = (String) dropdown1.getSelectedItem();
                String userY = (String) dropdown2.getSelectedItem();

                FavoritesDatabase favoritesDatabase = new FavoritesDatabase();
                List<String> userXFavorites = null;
                try {
                    userXFavorites = favoritesDatabase.getFavoriteUsers(userX);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                List<String> userYFavorites = null;
                try {
                    userYFavorites = favoritesDatabase.getFavoriteUsers(userY);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                List<String> commonFavorites = new ArrayList<>(userXFavorites);
                commonFavorites.retainAll(userYFavorites);


                JFrame searchResultFrame = new JFrame("Users favorited by both " + userX + " and " + userY);
                searchResultFrame.setSize(800, 500);
                searchResultFrame.setLocationRelativeTo(null);

                String[] columnNames = {"Username"};
                Object[][] data = new Object[commonFavorites.size()][1];
                if (!commonFavorites.isEmpty()) {
                    for (int i = 0; i < commonFavorites.size(); i++) {
                        String favorites = commonFavorites.get(i);
                        data[i][0] = favorites;
                    }
                }

                DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // Make all cells non-editable
                    }
                };

                JTable table = new JTable(tableModel);
                JScrollPane scrollPane = new JScrollPane(table);
                searchResultFrame.getContentPane().add(scrollPane);
                searchResultFrame.setVisible(true);
            });

            panel.add(dropdown1);
            panel.add(dropdown2);
            panel.add(searchButton);

            frame.add(panel);

            frame.setSize(300, 200);
            frame.setVisible(true);
        });



        usersWithoutAnyButton.addActionListener(e->{
            UserDatabase userDatabase = new UserDatabase();
            List<String> usersWithoutExcellentReviews = userDatabase.getUsersWithLessThanThreeExcellentReviews();

            JFrame searchResultFrame = new JFrame("Users without any Excellent items posted");
            searchResultFrame.setSize(800, 500);
            searchResultFrame.setLocationRelativeTo(null);

            String[] columnNames = {"Username"};
            Object[][] data = new Object[usersWithoutExcellentReviews.size()][1];
            if (!usersWithoutExcellentReviews.isEmpty()) {
                for (int i = 0; i < usersWithoutExcellentReviews.size(); i++) {
                    String userNoExcellentReviews = usersWithoutExcellentReviews.get(i);
                    data[i][0] = userNoExcellentReviews;
                }
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            searchResultFrame.getContentPane().add(scrollPane);
            searchResultFrame.setVisible(true);

        });


        allUsersWhoButton.addActionListener(e->{
            UserDatabase userDatabase = new UserDatabase();
            List<String> usersNoPoorReviews = userDatabase.getUsersWhoNeverPostedPoorReview();

            JFrame searchResultFrame = new JFrame("Users who have never left a poor review");
            searchResultFrame.setSize(800, 500);
            searchResultFrame.setLocationRelativeTo(null);

            String[] columnNames = {"Username"};
            Object[][] data = new Object[usersNoPoorReviews.size()][1];
            if (!usersNoPoorReviews.isEmpty()) {
                for (int i = 0; i < usersNoPoorReviews.size(); i++) {
                    String userNoPoorReviews = usersNoPoorReviews.get(i);
                    data[i][0] = userNoPoorReviews;
                }
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            searchResultFrame.getContentPane().add(scrollPane);
            searchResultFrame.setVisible(true);
        });


        allUserReviewsButton.addActionListener(e->{
            UserDatabase userDatabase = new UserDatabase();
            List<String> usersAllPoorReviews = userDatabase.getUsersWhoOnlyPostedPoorReviews();

            JFrame searchResultFrame = new JFrame("Users who only posted poor reviews");
            searchResultFrame.setSize(800, 500);
            searchResultFrame.setLocationRelativeTo(null);

            String[] columnNames = {"Username"};
            Object[][] data = new Object[usersAllPoorReviews.size()][1];
            if (!usersAllPoorReviews.isEmpty()) {
                for (int i = 0; i < usersAllPoorReviews.size(); i++) {
                    String userAllPoorReviews = usersAllPoorReviews.get(i);
                    data[i][0] = userAllPoorReviews;
                }
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            searchResultFrame.getContentPane().add(scrollPane);
            searchResultFrame.setVisible(true);
        });


        userHasNeverButton.addActionListener(e->{
            UserDatabase userDatabase = new UserDatabase();
            List<String> usersNoPoorReviews = userDatabase.getUsersWithNoPoorReviews();

            JFrame searchResultFrame = new JFrame("Users who have never received a poor review");
            searchResultFrame.setSize(800, 500);
            searchResultFrame.setLocationRelativeTo(null);

            String[] columnNames = {"Username"};
            Object[][] data = new Object[usersNoPoorReviews.size()][1];
            if (!usersNoPoorReviews.isEmpty()) {
                for (int i = 0; i < usersNoPoorReviews.size(); i++) {
                    String userNoPoorReviews = usersNoPoorReviews.get(i);
                    data[i][0] = userNoPoorReviews;
                }
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            searchResultFrame.getContentPane().add(scrollPane);
            searchResultFrame.setVisible(true);
        });

        userPairAButton.addActionListener(e->{
            UserDatabase userDatabase = new UserDatabase();
            userDatabase.getUserPairsWithExcellentReviews();
        });
    }
}
