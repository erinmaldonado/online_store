package com.example.login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;


public class UserOptions {
    public JPanel userOptionsPanel;
    private JButton postButton;
    private JButton buyButton;
    private JButton reviewButton;
    private JButton initializeButton;

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

    }

    private void createUIComponents() {

    }
}
