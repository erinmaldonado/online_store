package com.example.login;
import javax.swing.*;

public class Main {
	public static void main(String[] args){
		JFrame frame = new JFrame("Registration");
		frame.setContentPane(new Registration().panel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
