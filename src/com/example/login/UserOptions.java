package com.example.login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
            SwingUtilities.getWindowAncestor(postButton).dispose();

        });

        /*

        userXExcellentButton.addActionListener(e->{

        });

        usersWithMostButton.addActionListener(e->{

        });

        usersFavoritedByButton.addActionListener(e->{

        });

        usersWithoutAnyButton.addActionListener(e->{

        });

        allUsersWhoButton.addActionListener(e->{

        });

        allUserReviewsButton.addActionListener(e->{

        });

        userHasNeverButton.addActionListener(e->{

        });

        userPairAButton.addActionListener(e->{

        });
        */
    }
}
