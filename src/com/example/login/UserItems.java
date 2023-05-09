package com.example.login;
import java.time.LocalDate;
import java.util.Date;

public class UserItems {
    private final String username;
    private final String categoryName;
    private final String itemName;
    private final LocalDate date;

    public UserItems(String username, String categoryName, String itemName, LocalDate date) {
        this.username = username;
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.date = date;
    }

    public String getUsername(){
        return username;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getItemName(){
        return itemName;
    }

    public LocalDate getDate(){
        return date;
    }
}
