package com.example.login;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static javax.swing.SwingUtilities.getWindowAncestor;

public class ReviewItem {
    private final User currentUser;
    private JTextField searchTextField;
    private JButton searchButton;
    JPanel reviewItemPanel;
    private JButton backButton;

    public ReviewItem(User user){
        this.currentUser = user;

        searchButton.addActionListener(e -> {
            String category = searchTextField.getText();
            searchItemsByCategory(category);
        });

        backButton.addActionListener(e -> {
            JFrame frame = new JFrame("Menu");
            frame.setPreferredSize(new Dimension(400, 300));
            frame.setContentPane(new UserOptions(currentUser).userOptionsPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            getWindowAncestor(backButton).dispose();
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
            data[i][3] = "$"+item.getItemPrice();
            data[i][4] = category;
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
        selectFromTable(table);
        searchResultFrame.setVisible(true);
    }

    private void selectFromTable(JTable table){
        table.getSelectionModel().addListSelectionListener(e->{
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Object selectedItemID = table.getValueAt(selectedRow, 0);
                    int itemId = (Integer) selectedItemID;
                    showReviewForm(itemId);
                }
            }
        });
    }

    private void showReviewForm(int itemId) {
        JFrame reviewFrame = new JFrame("Review Item");
        reviewFrame.setSize(400, 200);
        reviewFrame.setLocationRelativeTo(null);
        reviewFrame.setLayout(new BorderLayout());

        JPanel reviewPanel = new JPanel(new FlowLayout());
        String[] ratings = {"Excellent", "Good", "Fair", "Poor"};
        JComboBox<String> ratingDropdown = new JComboBox<>(ratings);
        reviewPanel.add(ratingDropdown);

        JTextArea reviewDescription = new JTextArea(2, 30);
        reviewPanel.add(reviewDescription);

        JButton submitReviewButton = new JButton("Submit Review");
        submitReviewButton.addActionListener(e -> {
            try {
                ReviewsDatabase reviewsDb = new ReviewsDatabase();

                if(isItemOwner(currentUser.getUsername(), itemId)) {
                    JOptionPane.showMessageDialog(null, "You cannot review your own items.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (hasUserReviewedItem(currentUser.getUsername(), itemId)) {
                    JOptionPane.showMessageDialog(null, "You have already reviewed this item. You cannot review it again.", "Review exists", JOptionPane.WARNING_MESSAGE);
                } else if(reviewsDb.hasPostedThreeReviews(currentUser.getUsername())) {
                    JOptionPane.showMessageDialog(null, "You have reached the daily limit for reviews. You can submit a maximum of 3 reviews per day.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String score = (String) ratingDropdown.getSelectedItem();
                    String remark = reviewDescription.getText();
                    ReviewsDatabase reviewsDatabase = new ReviewsDatabase();
                    reviewsDatabase.insertReview(itemId, currentUser.getUsername(), score, remark);
                    JOptionPane.showMessageDialog(null, "Thank you for reviewing this item. Review has been added.", "Review added", JOptionPane.PLAIN_MESSAGE);
                    reviewFrame.dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        reviewPanel.add(submitReviewButton);
        reviewFrame.add(reviewPanel, BorderLayout.CENTER);
        reviewFrame.setVisible(true);
    }

    private boolean hasUserReviewedItem(String username, int itemId) throws SQLException {
        ReviewsDatabase reviewsDatabase = new ReviewsDatabase();
        return reviewsDatabase.userReviewedItem(username, itemId);
    }

    private boolean isItemOwner(String username, int itemId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM items WHERE item_id = ? AND username = ?")) {
            stmt.setInt(1, itemId);
            stmt.setString(2, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
