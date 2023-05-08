package com.example.login;
import javax.swing.JFrame;

public class UserOptionsFrame extends JFrame {
    private final User currentUser;

    public UserOptionsFrame(User user) {
        this.currentUser = user;
        setTitle("User Options");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        UserOptions userOptions = new UserOptions(user);
        setContentPane(userOptions.userOptionsPanel);

        // Automatically adjust the frame size based on its contents
        pack();
    }
}
