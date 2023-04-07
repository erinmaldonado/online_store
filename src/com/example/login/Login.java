package com.example.login;
import javax.swing.*;

import java.awt.event.ActionEvent;

import static javax.swing.SwingUtilities.getWindowAncestor;

public class Login {
    public JPanel loginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    public Login() {
        loginButton = new JButton("Login");
        loginButton.addActionListener(this::actionPerformed);

        backButton.addActionListener(e -> {
            JFrame frame = new JFrame("Home");
            frame.setContentPane(new Home().homePanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            getWindowAncestor(backButton).dispose();
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        backButton = new JButton("Back");
    }

    private void actionPerformed(ActionEvent e) {
        String un = usernameField.getText();
        String pw = String.valueOf(passwordField.getPassword());

        UserDatabase userDB = new UserDatabase();
        User user = userDB.loginUser(un, pw);

        if (user != null) {
            JFrame userOptionsFrame = new UserOptionsFrame();
            userOptionsFrame.setVisible(true);
            getWindowAncestor(loginButton).dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password.");
        }
    }
}
