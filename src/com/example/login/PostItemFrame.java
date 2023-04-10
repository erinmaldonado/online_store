package com.example.login;

import javax.swing.*;

public class PostItemFrame extends JFrame{
    private final User currentUser;

    public PostItemFrame(User user) {
        this.currentUser = user;

        setTitle("Post Item");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add the post item form
        PostItem postItemForm = new PostItem(currentUser);
        getContentPane().add(postItemForm.postItemPanel);

    }
}
