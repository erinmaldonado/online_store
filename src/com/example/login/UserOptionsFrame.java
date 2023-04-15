package com.example.login;
import javax.swing.JFrame;

public class UserOptionsFrame extends JFrame {
    private final User currentUser;

    public UserOptionsFrame(User user) {
        this.currentUser = user;
        setTitle("Menu");
        setSize(400, 300);
        UserOptions userOptions = new UserOptions(currentUser);
        getContentPane().add(userOptions.userOptionsPanel);
    }
}
