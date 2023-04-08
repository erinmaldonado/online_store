package com.example.login;
import javax.swing.*;

public class Main {
	public static void main(String[] args){
		JFrame frame = new JFrame("Home");
		frame.setContentPane(new Home().homePanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
