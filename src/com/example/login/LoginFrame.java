package com.example.login;
import javax.swing.JFrame;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add the login form
        Login login = new Login();
        getContentPane().add(login.loginPanel);
    }

}
