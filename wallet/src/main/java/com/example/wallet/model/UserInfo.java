package com.example.wallet.model;

public class UserInfo {
    private final String userId;
    private final String username;

    public UserInfo(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}