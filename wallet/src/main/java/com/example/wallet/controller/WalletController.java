package com.example.wallet.controller;
import com.example.wallet.model.CustomUserDetails;

import com.example.wallet.model.UserInfo;
import com.example.wallet.model.Wallet;
import com.example.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }


    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);


    @GetMapping
    public ResponseEntity<?> getUserWallets() {
        String userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        Optional<Wallet> wallet = walletService.getWalletByUserId(userId);

        if (wallet.isPresent()) {
            // Return the wallet as a single-item list
            return ResponseEntity.ok(Collections.singletonList(wallet.get()));
        } else {
            // Return an empty list if no wallet exists
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWalletById(@PathVariable String id) {
        String userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        Optional<Wallet> wallet = walletService.getWalletById(id);

        if (wallet.isPresent()) {
            // Ensure the wallet belongs to the current user
            if (wallet.get().getUserId().equals(userId)) {
                return ResponseEntity.ok(wallet.get());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "You don't have access to this wallet"));
            }
        }

        return ResponseEntity.notFound().build();
    }

    private UserInfo getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return new UserInfo(userDetails.getUserId(), userDetails.getUsername());
        }
        return null;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWallet() {
        UserInfo userInfo = getCurrentUserInfo();
        System.out.println("here we are get");
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        String userId = userInfo.getUserId();
        String username = userInfo.getUsername();

        // Log request for debugging
        logger.info("Create wallet request received for user: {}", userId);

        // Check if user already has a wallet
        Optional<Wallet> existingWallet = walletService.getWalletByUserId(userId);
        if (existingWallet.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "User already has a wallet",
                            "wallet", existingWallet.get()
                    ));
        }

        String currency = "INR";

        try {
            Wallet newWallet = walletService.createWallet(userId, username, currency);
            return ResponseEntity.status(HttpStatus.CREATED).body(newWallet);
        } catch (Exception e) {
            logger.error("Error creating wallet: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }



       @GetMapping("/getWallet")
        public ResponseEntity<?> getWallet() {
        UserInfo userInfo = getCurrentUserInfo();
        System.out.println("here we are get");
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        String userId = userInfo.getUserId();
        String username = userInfo.getUsername();

        // Log request for debugging
        logger.info("Create wallet request received for user: {}", userId);

        // Check if user already has a wallet
        Optional<Wallet> existingWallet = walletService.getWalletByUserId(userId);
        if (existingWallet.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "User already has a wallet",
                            "wallet", existingWallet.get()
                    ));
        }

           String currency = "INR";

        try {
            Wallet newWallet = walletService.createWallet(userId, username, currency);
            return ResponseEntity.status(HttpStatus.OK).body(newWallet);
        } catch (Exception e) {
            logger.error("Error creating wallet: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }



    @GetMapping("/balance")
    public ResponseEntity<?> getWalletBalance() {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        String userId = userInfo.getUserId();

        // Get user's wallet
        Optional<Wallet> walletOpt = walletService.getWalletByUserId(userId);

        if (walletOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No wallet found for user"));
        }

        Wallet wallet = walletOpt.get();

        return ResponseEntity.ok(Map.of(
                "walletId", wallet.getId(),
                "userId", wallet.getUserId(),
                "owner", wallet.getOwner(),
                "balance", wallet.getBalance(),
                "currency", wallet.getCurrency(),
                "lastUpdated", wallet.getUpdatedAt()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWallet(@PathVariable String id) {
        String userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        Optional<Wallet> wallet = walletService.getWalletById(id);

        if (wallet.isPresent()) {
            // Ensure the wallet belongs to the current user
            if (wallet.get().getUserId().equals(userId)) {
                boolean deleted = walletService.deleteWallet(id);
                if (deleted) {
                    return ResponseEntity.ok(Map.of("message", "Wallet deleted successfully"));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "Failed to delete wallet"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "You don't have access to this wallet"));
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return ResponseEntity.ok(Map.of(
                    "userId", userDetails.getUserId(),
                    "username", userDetails.getUsername()
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "User not authenticated"));
    }

    // Helper method to get the current user ID
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUserId();
        }
        return null;
    }
}
