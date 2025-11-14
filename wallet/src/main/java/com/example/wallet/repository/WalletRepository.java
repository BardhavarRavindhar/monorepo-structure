package com.example.wallet.repository;

import com.example.wallet.model.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends MongoRepository<Wallet, String> {
    boolean existsByUserId(String userId);
    Optional<Wallet> findByUserId(String userId);

    Wallet getWalletsById(String id);
}