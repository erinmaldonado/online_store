package com.example.login;

public class Review {
    private String username;
    private String rating;
    private String description;

    public Review(String username, String rating, String description) {
        this.username = username;
        this.rating = rating;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }
}
