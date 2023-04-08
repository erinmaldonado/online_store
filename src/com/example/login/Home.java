package com.example.login;

import javax.swing.*;

import static javax.swing.SwingUtilities.getWindowAncestor;

public class Home {
    private JButton loginButton;
    private JButton registerButton;
    JPanel homePanel;

    public Home(){
        loginButton.addActionListener(e -> {
            // Create a new instance of the LoginFrame class
            JFrame loginFrame = new LoginFrame();
            // Set the login frame to be visible
            loginFrame.setVisible(true);
            // Close the current home frame
            getWindowAncestor(loginButton).dispose();
        });

        registerButton.addActionListener(e -> {
            // Create a new instance of the RegistrationFrame class
            JFrame registrationFrame = new RegistrationFrame();
            // Set the registration frame to be visible
            registrationFrame.setVisible(true);
            // Close the current home frame
            getWindowAncestor(registerButton).dispose();
        });
    }
}
