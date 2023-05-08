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

            // Create a JTable with column names and the retrieved items
            String[] columnNames = {"ID", "Name", "Description", "Price"};
            Object[][] data = new Object[items.size()][5];
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                data[i][0] = item.getItemId();
                data[i][1] = item.getItemName();
                data[i][2] = item.getItemDescription();
                data[i][3] = "$"+item.getItemPrice();
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
            ItemsDatabase itemsDatabase = new ItemsDatabase();
            List<Item> mostExpensiveItems = itemsDatabase.getMostExpensiveItemsInEachCategory();

            JFrame frame = new JFrame("Most Expensive Items in Each Category");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            frame.setLocationRelativeTo(null);

            if (!mostExpensiveItems.isEmpty()) {
                String[] columnNames = {"Item ID", "Title", "Description", "Price", "Username", "Category"};
                Object[][] data = new Object[mostExpensiveItems.size()][6];

                for (int i = 0; i < mostExpensiveItems.size(); i++) {
                    Item item = mostExpensiveItems.get(i);
                    data[i][0] = item.getItemId();
                    data[i][1] = item.getItemName();
                    data[i][2] = item.getItemDescription();
                    data[i][3] = String.format("$%.2f", item.getItemPrice());
                    data[i][4] = item.getUsername();
                    data[i][5] = item.getCategoryName();
                }

                DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // Make all cells non-editable
                    }
                };

                JTable table = new JTable(tableModel);
                JScrollPane scrollPane = new JScrollPane(table);
                frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
            } else {
                JOptionPane.showMessageDialog(null, "No items found in the categories.", "No items.", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.setVisible(true);
        });

        /*
        usersTwoItemsButton.addActionListener(e->{

        });

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
