package com.example.login;

import javax.swing.*;

public class PostItemFrame extends JFrame{
    private final User currentUser;

    public PostItemFrame(User user) {
        this.currentUser = user;

        setTitle("Post Item");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Add the post item form
        PostItem postItemForm = new PostItem(currentUser);
        setContentPane(postItemForm.postItemPanel);

        // Automatically adjust the frame size based on its contents
        pack();
    }
}
