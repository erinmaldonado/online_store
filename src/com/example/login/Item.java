package com.example.login;

/**
 * Represents an item.
 * @author Erin Maldonado
 */
public class Item {
    private int itemId;
    private String itemName;
    private String itemDescription;
    private double itemPrice;
    private String datePosted;
    private String username;

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
    public Item(int itemId, String itemName, String itemDescription, String datePosted, double itemPrice, String username) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.datePosted = datePosted;
        this.itemPrice = itemPrice;
        this.username = username;
    }

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

}