package com.example.login;

import javax.swing.*;

public class EnterCategoriesFrame extends JFrame {
    public EnterCategoriesFrame(){
        setTitle("Enter users");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Add the login form
        EnterCategories enterCategories = new EnterCategories();
        setContentPane(enterCategories.searchUsersPanel);

        // Automatically adjust the frame size based on its contents
        pack();
    }
}
