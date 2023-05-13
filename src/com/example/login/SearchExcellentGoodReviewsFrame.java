package com.example.login;

import javax.swing.*;

public class SearchExcellentGoodReviewsFrame extends JFrame {
    public SearchExcellentGoodReviewsFrame() {
        setTitle("Input a user to search reviews");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Add the post item form
        SearchExcellentGoodReviews searchExcellentGoodReviewsForm = new SearchExcellentGoodReviews();
        setContentPane(searchExcellentGoodReviewsForm.reviewsPanel);

        // Automatically adjust the frame size based on its contents
        pack();
    }
}
