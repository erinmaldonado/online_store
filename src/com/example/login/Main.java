package com.example.login;
import javax.swing.*;
import java.awt.*;

public class Main {
	public static void main(String[] args){
		CreateTables createTables = new CreateTables();
		createTables.create();

		JFrame frame = new JFrame("Home");
		frame.setContentPane(new Home().homePanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Center the frame on the screen

		// Automatically adjust the frame size based on its contents
		frame.pack();
		frame.setVisible(true);
	}
}
