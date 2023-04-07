package com.example.login;
import javax.swing.JFrame;

public class UserOptionsFrame extends JFrame {

    public UserOptionsFrame() {
        UserOptions userOptions = new UserOptions();
        setContentPane(userOptions.userOptionsPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
