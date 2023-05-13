package com.example.login;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private List<Item> itemList = new ArrayList<>();
	private List<User> userFavorites = new ArrayList<>();

	User(){

	}

	User(String username){
		this.username = username;
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

	public int getNumberOfItemsPosted(){
		return itemList.size();
	}

	public void setUserItems(List<Item> itemList){
		this.itemList = itemList;
	}

	public void setUserFavorites(List<User> userFavorites){
		this.userFavorites = userFavorites;
	}

	public void addUserItem(Item item){
		itemList.add(item);
	}
}
