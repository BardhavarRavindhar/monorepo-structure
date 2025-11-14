package com.example.wallet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "Welcome to Wallet Application! Access the API at /api/wallets";
    }

    // You can add more endpoints as needed
    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello, Spring Boot!";
    }
}