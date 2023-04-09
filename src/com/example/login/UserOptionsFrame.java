package com.example.login;
import javax.swing.JFrame;

public class UserOptionsFrame extends JFrame {
    private User currentUser;

    public UserOptionsFrame(User user) {
        this.currentUser = user;
        UserOptions userOptions = new UserOptions(currentUser);
        setContentPane(userOptions.userOptionsPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
