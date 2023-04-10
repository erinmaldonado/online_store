package com.example.login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class UserOptions {
    public JPanel userOptionsPanel;
    private JButton postButton;
    private JButton buyButton;
    private JButton reviewButton;
    private JButton initializeButton;

    private final User currentUser;

    public UserOptions(User user) {
        this.currentUser = user;
        postButton.addActionListener(e -> {
            JFrame postItemFrame = new PostItemFrame(currentUser);
            postItemFrame.setVisible(true);
            SwingUtilities.getWindowAncestor(postButton).dispose();
        });

        reviewButton.addActionListener(e->{
            JFrame reviewItemFrame = new ReviewItemFrame(currentUser);
            reviewItemFrame.setVisible(true);
            SwingUtilities.getWindowAncestor(reviewButton).dispose();
        });

    }

    private void createUIComponents() {

    }
}
