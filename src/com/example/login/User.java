package com.example.login;

import java.util.List;

public class User {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private List<Item> itemList;

	User(){

	}

	User(String username, String firstName, String lastName){
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	User(String username, String password, String firstName, String lastName, String email){
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getUsername(){
		return username;
	}

	public  String getPassword(){
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public List<Item> getItemList(){
		return itemList;
	}

	public void setUserItems(List<Item> itemList){
		this.itemList = itemList;
	}
}
