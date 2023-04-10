package com.example.login;

public class Item {
    private int itemId;
    private String itemName;
    private String itemDescription;
    private double itemPrice;
    private String datePosted;
    private String username;

    public Item() {
    }

    public Item(int itemId, String itemName, String itemDescription, String datePosted, double itemPrice, String username) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.datePosted = datePosted;
        this.itemPrice = itemPrice;
        this.username = username;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public double getItemPrice() {
        return itemPrice;
    }

}