package com.example.login;
import javax.swing.*;
import java.awt.*;

public class Main {
	public static void main(String[] args){
		CreateTables createTables = new CreateTables();
		createTables.create();

		JFrame frame = new JFrame("Home");
		frame.setPreferredSize(new Dimension(400, 300));
		frame.setContentPane(new Home().homePanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
