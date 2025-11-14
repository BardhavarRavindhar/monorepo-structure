package com.example.wallet.service;

import com.example.wallet.model.Transaction;
import com.example.wallet.model.Wallet;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public Transaction deductAmount(String walletId, BigDecimal amount, String description, String reference) {
        // Validate inputs
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        // Find wallet
        Optional<Wallet> walletOpt = walletRepository.findById(walletId);
        if (walletOpt.isEmpty()) {
            throw new IllegalArgumentException("Wallet not found");
        }

        Wallet wallet = walletOpt.get();

        // Check sufficient balance
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Update wallet balance
        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setWalletId(walletId);
        transaction.setUserId(wallet.getUserId());
        transaction.setType(Transaction.TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription(description);
        transaction.setReference(reference);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction addAmount(String walletId, BigDecimal amount, String description, String reference) {
        // Validate inputs
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        // Find wallet
        Optional<Wallet> walletOpt = walletRepository.findById(walletId);
        if (walletOpt.isEmpty()) {
            throw new IllegalArgumentException("Wallet not found");
        }

        Wallet wallet = walletOpt.get();

        // Update wallet balance
        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setWalletId(walletId);
        transaction.setUserId(wallet.getUserId());
        transaction.setType(Transaction.TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription(description);
        transaction.setReference(reference);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getWalletTransactions(String walletId) {
        return transactionRepository.findByWalletIdOrderByTimestampDesc(walletId);
    }

    public Page<Transaction> getWalletTransactionsPaged(String walletId, Pageable pageable) {
        return transactionRepository.findByWalletId(walletId, pageable);
    }

    public List<Transaction> getUserTransactions(String userId) {
        return transactionRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public List<Transaction> getTransactionsByPeriod(String walletId, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByWalletIdAndTimestampBetween(walletId, start, end);
    }

    public List<Transaction> getTransactionsByType(String walletId, Transaction.TransactionType type) {
        return transactionRepository.findByWalletIdAndType(walletId, type);
    }
    // Paginated transaction history
    public Page<Transaction> getWalletTransactionsPaged(String walletId, int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return transactionRepository.findByWalletId(walletId, pageable);
    }

    // Get transactions by userId with pagination
    public Page<Transaction> getUserTransactionsPaged(String userId, int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return transactionRepository.findByUserId(userId, pageable);
    }

    public List<Transaction> getTransactionsByMonth(String walletId, int year, int month) {
        // Create date range for the specified month
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

        return transactionRepository.findByWalletIdAndTimestampBetween(walletId, startOfMonth, endOfMonth);
    }

    public Page<Transaction> getFilteredTransactions(
            String walletId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String typeStr,
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Handle null dates
        LocalDateTime start = startDate != null ? startDate : LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime end = endDate != null ? endDate : LocalDateTime.now();

        // Handle type filter
        if (typeStr != null && !typeStr.isEmpty()) {
            try {
                Transaction.TransactionType type = Transaction.TransactionType.valueOf(typeStr.toUpperCase());
                return transactionRepository.findByWalletIdAndTimestampBetweenAndType(
                        walletId, start, end, type, pageable);
            } catch (IllegalArgumentException e) {
                // Invalid transaction type, ignore type filter
                return transactionRepository.findByWalletIdAndTimestampBetween(
                        walletId, start, end, pageable);
            }
        } else {
            // No type filter
            return transactionRepository.findByWalletIdAndTimestampBetween(
                    walletId, start, end, pageable);
        }
    }
}