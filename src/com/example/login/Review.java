package com.example.login;

public class Review {
    private final String username;
    private final String score;
    private final String remark;
    private int itemId;

    public Review(String username, String score, String remark) {
        this.username = username;
        this.score = score;
        this.remark = remark;
    }

    public void setItemId(int itemId){
        this.itemId = itemId;
    }

    public int getItemId(){
        return itemId;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return remark;
    }

    public Object getScore() {
        return score;
    }
}
