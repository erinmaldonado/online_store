package com.example.login;
import javax.swing.JFrame;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Add the login form
        Login login = new Login();
        setContentPane(login.loginPanel);

        // Automatically adjust the frame size based on its contents
        pack();

    }

}
