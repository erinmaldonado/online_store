package com.example.login;

import javax.swing.*;
import java.util.List;

public class UserOptions {
    public JPanel userOptionsPanel;
    private JButton postButton;
    private JButton buyButton;
    private JButton reviewButton;
    private JButton initializeButton;
    private JTextField searchTextField;
    private JButton searchButton;

    private User currentUser;

    public UserOptions(User user) {
        this.currentUser = user;
        postButton.addActionListener(e -> {
            JFrame postItemFrame = new PostItemFrame(currentUser);
            postItemFrame.setVisible(true);
            SwingUtilities.getWindowAncestor(postButton).dispose();
        });

        searchButton.addActionListener(e -> {
            String category = searchTextField.getText();
            searchItemsByCategory(category);
        });

    }

    private void searchItemsByCategory(String category) {
        ItemsDatabase itemsDb = new ItemsDatabase();
        List<Item> items = itemsDb.searchItemsByCategory(category);
        JFrame searchResultFrame = new JFrame("Search Results");
        searchResultFrame.setSize(500, 500);
        searchResultFrame.setLocationRelativeTo(null);

        // Create a JTable with column names and the retrieved items
        String[] columnNames = {"ID", "Name", "Description", "Price", "Category"};
        Object[][] data = new Object[items.size()][5];
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            data[i][0] = item.getItemId();
            data[i][1] = item.getItemName();
            data[i][2] = item.getItemDescription();
            data[i][3] = item.getItemPrice();
            data[i][4] = item.getItemCategory();
        }
        JTable table = new JTable(data, columnNames);

        // Add the table to a scroll pane and add the scroll pane to the frame
        JScrollPane scrollPane = new JScrollPane(table);
        searchResultFrame.getContentPane().add(scrollPane);

        searchResultFrame.setVisible(true);
    }

    private void createUIComponents() {

    }
}
