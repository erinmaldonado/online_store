package com.example.login;

import java.sql.Date;
import java.util.List;

/**
 * Represents an item.
 * @author Erin Maldonado
 */
public class Item {
    private int itemId;
    private String itemName;
    private String itemDescription;
    private double itemPrice;
    private Date datePosted;
    private String username;
    private String categoryName;
    private List<String> categoriesList;


    private String categoryNameX;
    private String categoryNameY;

    /**
     * Class constructor
     */
    public Item() {
    }

    /**
     * Creates an item with a specified id, name, description,
     * date, price, and username.
     * @param itemId
     * @param itemName The name of the item.
     * @param itemDescription The description of the item.
     * @param datePosted The date the item was posted.
     * @param itemPrice The price of the item.
     * @param username The username of the item's creator.
     */
    public Item(int itemId, String itemName, String itemDescription, Date datePosted, double itemPrice, String username) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.datePosted = datePosted;
        this.itemPrice = itemPrice;
        this.username = username;
    }

    //Getters
    /**
     * Returns the item's id which has been generated
     * automatically  using auto-increment feature
     * @return item's id
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Returns the item's name which is input by
     * the user.
     * @return item's name
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Returns the item's description which is input
     * by the user.
     * @return item's description
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * Returns the item's price which is input
     * by the user.
     * @return item's price
     */
    public double getItemPrice() {
        return itemPrice;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public String getUsername() {
        return username;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<String> getCategoryNames() {
        return categoriesList;
    }

    public String getCategoryNameX(){
        return categoryNameX;
    }

    public String getCategoryNameY(){
        return categoryNameY;
    }

    // End Getters


    // Beginning Setters

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }


    public void setDatePosted(Date datePosted){
        this.datePosted = datePosted;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public void setCategoryNames(List<String> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public void setCategoryNameX(String categoryNameX) {
        this.categoryNameX = categoryNameX;
    }
    public void setCategoryNameY(String categoryNameY) {
        this.categoryNameY = categoryNameY;
    }

// End Setters



    @Override
    public String toString(){
        return "Item id: " + itemId + "\n" +
                "Item Name: " + itemName + "\n" +
                "Item Description: " +itemDescription + "\n" +
                "Item Price: " + itemPrice + "\n" +
                "Date Posted: " + datePosted + "\n" +
                "Username: " + username + "\n" +
                "Category Name: " + categoryName + "\n" +
                "Categories List: " + categoriesList;
    }
}