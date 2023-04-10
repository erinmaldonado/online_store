package com.example.login;
import javax.swing.JFrame;

public class UserOptionsFrame extends JFrame {
    private final User currentUser;

    public UserOptionsFrame(User user) {
        this.currentUser = user;
        setTitle("Menu");
        UserOptions userOptions = new UserOptions(currentUser);
        setContentPane(userOptions.userOptionsPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
