package com.example.wallet.config;

import org.springframework.beans.factory.annotation.Value;

public class VariableAccess {

    @Value("${app.money}")
    private int money;

    public int getMoney() {
        return money;
    }
}
