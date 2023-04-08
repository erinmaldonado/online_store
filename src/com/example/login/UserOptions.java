package com.example.login;

import javax.swing.*;

public class UserOptions {
    public JPanel userOptionsPanel;
    private JButton postButton;
    private JButton buyButton;
    private JButton reviewButton;
    private JButton initializeButton;

    public UserOptions() {
        postButton = new JButton("Post");
        /*postButton.addActionListener(e -> {
            JFrame postItemFrame = new PostItemFrame();
            postItemFrame.setVisible(true);
            SwingUtilities.getWindowAncestor(postButton).dispose();
        });*/
    }

    private void createUIComponents() {

    }
}
