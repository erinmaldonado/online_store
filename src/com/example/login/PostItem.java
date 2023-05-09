package com.example.login;

import javax.swing.*;

import java.awt.*;

import static javax.swing.SwingUtilities.getWindowAncestor;

public class PostItem {
	private JTextField titleField;
	private JTextField priceField;
	private JButton postButton;
	JPanel postItemPanel;
	private JTextField categoryField;
	private JButton backButton;
	private JTextArea descriptionTextArea;

	private final User currentUser;

	public PostItem(User user){
		this.currentUser = user;

		postButton.addActionListener(e -> {
						ItemsDatabase itemsDatabase = new ItemsDatabase();
			String currentUsername = currentUser.getUsername();

			int itemsPostedToday = itemsDatabase.getItemsPostedToday(currentUsername);

			if (itemsPostedToday < 3) {
				String titleFieldText = titleField.getText();
				String descriptionTextAreaText = descriptionTextArea.getText();
				String[] categoryFieldText = categoryField.getText().split(",");

				try {
					double priceFieldText = Double.parseDouble(priceField.getText());

					int itemId = itemsDatabase.insertItem(titleFieldText, descriptionTextAreaText, priceFieldText, currentUsername);
					itemsDatabase.insertItemCategories(itemId, categoryFieldText);

					JOptionPane.showMessageDialog(null, "Item posted.");
					// go back to user options page
					JFrame userOptionsFrame = new UserOptionsFrame(currentUser);
					userOptionsFrame.setVisible(true);
					SwingUtilities.getWindowAncestor(postButton).dispose();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Please enter a valid price.");
				}
			} else {
				JOptionPane.showMessageDialog(null, "You can only post 3 items per day.");
			}
		});

		backButton.addActionListener(e -> {
			JFrame frame = new JFrame("Menu");
			frame.setPreferredSize(new Dimension(400, 300));
			frame.setContentPane(new UserOptions(currentUser).userOptionsPanel);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
			getWindowAncestor(backButton).dispose();
		});
	}

	private void createUIComponents() {
		descriptionTextArea = new JTextArea(10, 30); // 10 rows and 30 columns
	}
}
