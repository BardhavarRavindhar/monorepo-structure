package com.example.wallet.repository;

import com.example.wallet.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Page<Transaction> findByUserId(String userId, Pageable pageable);

    // Filtered pagination methods
    Page<Transaction> findByWalletIdAndTimestampBetween(
            String walletId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Transaction> findByWalletIdAndType(
            String walletId, Transaction.TransactionType type, Pageable pageable);

    Page<Transaction> findByWalletIdAndTimestampBetweenAndType(
            String walletId, LocalDateTime start, LocalDateTime end,
            Transaction.TransactionType type, Pageable pageable);
    List<Transaction> findByWalletId(String walletId);
    List<Transaction> findByWalletIdOrderByTimestampDesc(String walletId);
    List<Transaction> findByUserId(String userId);
    List<Transaction> findByUserIdOrderByTimestampDesc(String userId);
    Page<Transaction> findByWalletId(String walletId, Pageable pageable);
    List<Transaction> findByWalletIdAndTimestampBetween(String walletId, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByWalletIdAndType(String walletId, Transaction.TransactionType type);
}