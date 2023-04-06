package com.example.login;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration {
	public JPanel panel1;
	JTextField username;
	private JPasswordField password;
	private JTextField firstName;
	private JTextField lastName;
	private JTextField email;
	private JButton signUpButton;

	public Registration(){
		signUpButton.addActionListener(e -> {
			String un = username.getText();
			String pw = String.valueOf(password.getPassword());
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
			if(user!=null){
				JOptionPane.showMessageDialog(null, "User already exists.");
			} else{
				user = new User();
				user.setUsername(un);
				user.setPassword(pw);
				user.setFirstName(fn);
				user.setLastName(ln);
				user.setEmail(em);
				boolean isRegistered = userDB.registerUser(user);
				if(isRegistered){
					JOptionPane.showMessageDialog(null, "User registered.");
				} else {
					JOptionPane.showMessageDialog(null, "Error registering user. Try again.");
				}
			}
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
		username = new JTextField("username");
	}

	private void $$$setupUI$$$() {
		createUIComponents();
	}
}
