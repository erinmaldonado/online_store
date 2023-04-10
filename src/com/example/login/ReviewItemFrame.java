package com.example.login;

import javax.swing.*;

public class ReviewItemFrame extends JFrame{
    private final User currentUser;

    public ReviewItemFrame(User user) {
        this.currentUser = user;

        setTitle("Post Item");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add the post item form
        ReviewItem reviewItemForm = new ReviewItem(currentUser);
        getContentPane().add(reviewItemForm.reviewItemPanel);
    }
}
