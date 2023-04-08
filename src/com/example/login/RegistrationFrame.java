package com.example.login;

import javax.swing.*;

public class RegistrationFrame extends JFrame {
    public RegistrationFrame() {
        setTitle("Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add the registration form
        Registration registrationForm = new Registration();
        getContentPane().add(registrationForm.registrationPanel);
    }
}
