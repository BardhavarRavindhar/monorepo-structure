package com.example.wallet.model;

import java.io.Serializable;

public class CustomUserDetails implements Serializable {
    private final String username;
    private final String userId;

    public CustomUserDetails(String username, String userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return username;
    }
}