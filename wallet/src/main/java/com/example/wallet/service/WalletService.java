package com.example.wallet.service;

import com.example.wallet.model.Wallet;
import com.example.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }
    public List<Wallet> getWalletsByUserId(String userId) {

        Optional<Wallet> wallet = walletRepository.findByUserId(userId);
        if (wallet.isPresent()) {
            return Collections.singletonList(wallet.get());
        }
        return Collections.emptyList();
    }


    public Optional<Wallet> getWalletById(String walletId) {
        return walletRepository.findById(walletId);
    }
    public Optional<Wallet> getWalletByUserId(String userId) {
        return walletRepository.findByUserId(userId);
    }

    public Wallet createWallet(String userId, String ownerName, String currency) {
        Optional<Wallet> existingWallet = walletRepository.findByUserId(userId);

        // If wallet exists, return it
        if (existingWallet.isPresent()) {
            return existingWallet.get();
        }
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setOwner(ownerName);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency(currency);
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setUpdatedAt(LocalDateTime.now());

        return walletRepository.save(wallet);
    }

    public Optional<Wallet> updateWalletBalance(String userID, BigDecimal newBalance) {
        Optional<Wallet> walletOpt = walletRepository.findById(userID);

        if (walletOpt.isPresent()) {
            Wallet wallet = walletOpt.get();
            wallet.setBalance(newBalance);
            wallet.setUpdatedAt(LocalDateTime.now());
            return Optional.of(walletRepository.save(wallet));
        }

        return Optional.empty();
    }

    public boolean deleteWallet(String userID) {
        if (walletRepository.existsById(userID)) {
            walletRepository.deleteById(userID);
            return true;
        }
        return false;
    }


}