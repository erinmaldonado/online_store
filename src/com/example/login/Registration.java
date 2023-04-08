package com.example.login;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.SwingUtilities.getWindowAncestor;

public class Registration {
    public JPanel registrationPanel;
    private JPasswordField passwordField;
    private JTextField firstName;
    private JTextField lastName;
    private JTextField email;
    private JButton signUpButton;
    private JButton backButton;
    private JTextField usernameField;

    public Registration() {
        signUpButton.addActionListener(e -> {
            String un = usernameField.getText();
            String pw = String.valueOf(passwordField.getPassword());
            String fn = firstName.getText();
            String ln = lastName.getText();
            String em = email.getText();

            // Make sure email is in the correct format
            if (!isValidEmail(em)) {
                JOptionPane.showMessageDialog(null, "Invalid email format. Please enter a valid email.");
                return;
            }

            UserDatabase userDB = new UserDatabase();
            User user = userDB.loginUser(un, pw);

            // Check if user is already in system
            // If not, add to database
            if (user != null) {
                JOptionPane.showMessageDialog(null, "User already exists. Go back to Login.");
            } else {
                user = new User();
                user.setUsername(un);
                user.setPassword(pw);
                user.setFirstName(fn);
                user.setLastName(ln);
                user.setEmail(em);
                boolean isRegistered = userDB.registerUser(user);
                if (isRegistered) {
                    JFrame userOptionsFrame = new UserOptionsFrame();
                    // Set the login frame to be visible
                    userOptionsFrame.setVisible(true);
                    // Close the current home frame
                    getWindowAncestor(signUpButton).dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Error registering user. Try again.");
                }
            }
        });

        backButton.addActionListener(e -> {
            JFrame frame = new JFrame("Home");
            frame.setContentPane(new Home().homePanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            SwingUtilities.getWindowAncestor(backButton).dispose();
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void createUIComponents() {
        // TODO: place custom component creation code here
        usernameField = new JTextField("username");
    }

    private void $$$setupUI$$$() {
        createUIComponents();
    }
}
