package com.example.login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class SearchExcellentGoodReviews {
    private JTextField usernameTextField;
    private JButton searchButton;
    JPanel reviewsPanel;
    private JButton backButton;

    public SearchExcellentGoodReviews(){
        UserDatabase userDatabase = new UserDatabase();
        ReviewsDatabase reviewsDatabase = new ReviewsDatabase();

        searchButton.addActionListener(e->{
            String usernameText = usernameTextField.getText();

            List<Item> userItems = userDatabase.getUserItems(usernameText);
            List<Review> goodOrExcellentReviews = new ArrayList<>();

            for (Item item : userItems) {
                List<Review> itemReviews = reviewsDatabase.getReviewsForItem(item.getItemId());

                for (Review review : itemReviews) {
                    if (review.getScore().equals("Excellent") || review.getScore().equals("Good")) {
                        review.setItemId(item.getItemId());
                        goodOrExcellentReviews.add(review);
                    }
                }
            }

            for (Review review : goodOrExcellentReviews) {
                System.out.println(review);
            }

            JFrame searchResultFrame = new JFrame("Items with Excellent or Good Reviews");
            searchResultFrame.setSize(800, 500);
            searchResultFrame.setLocationRelativeTo(null);

            String[] columnNames = {"Item Id", "Reviewer Username", "Score", "Remark"};
            Object[][] data = new Object[goodOrExcellentReviews.size()][5];
            if (!goodOrExcellentReviews.isEmpty()) {
                for (int i = 0; i < goodOrExcellentReviews.size(); i++) {
                    Review review = goodOrExcellentReviews.get(i);

                    data[i][0] = review.getItemId();
                    data[i][1] = review.getUsername();
                    data[i][2] = review.getScore();
                    data[i][3] = review.getDescription();

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

    }
}
