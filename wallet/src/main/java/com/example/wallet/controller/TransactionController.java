package com.example.wallet.controller;

import com.example.wallet.model.CustomUserDetails;
import com.example.wallet.model.Transaction;
import com.example.wallet.model.UserInfo;
import com.example.wallet.model.Wallet;
import com.example.wallet.service.PdfService;
import com.example.wallet.service.TransactionService;
import com.example.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final PdfService pdfService;
    private final WalletService walletService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    public TransactionController(TransactionService transactionService, PdfService pdfService, WalletService walletService) {
        this.transactionService = transactionService;
        this.pdfService = pdfService;
        this.walletService = walletService;
    }



    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawFromWallet(@RequestBody Map<String, Object> request) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        try {
            String userId = userInfo.getUserId();

            // Check if user has a wallet
            Optional<Wallet> walletOpt = walletService.getWalletByUserId(userId);
            if (walletOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No wallet found for user"));
            }

            // Extract request parameters
            String walletId = walletOpt.get().getId();
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String description = (String) request.getOrDefault("description", "Withdrawal");
            String reference = (String) request.getOrDefault("reference", "");

            // Process withdrawal
            Transaction transaction = transactionService.deductAmount(walletId, amount, description, reference);

            return ResponseEntity.ok(transaction);
        } catch (IllegalArgumentException e) {
            logger.error("Error in withdrawal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error in withdrawal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> depositToWallet(@RequestBody Map<String, Object> request) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }
        System.out.println(userInfo.getUserId());
        try {
            String userId = userInfo.getUserId();

            // Check if user has a wallet
            Optional<Wallet> walletOpt = walletService.getWalletByUserId(userId);
            if (walletOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No wallet found for user" +userId));
            }

            // Extract request parameters
            String walletId = walletOpt.get().getId();
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String description = (String) request.getOrDefault("description", "Deposit");
            String reference = (String) request.getOrDefault("reference", "");

            // Process deposit
            Transaction transaction = transactionService.addAmount(walletId, amount, description, reference);

            return ResponseEntity.ok(transaction);
        } catch (IllegalArgumentException e) {
            logger.error("Error in deposit: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error in deposit", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @GetMapping("/history/pdf/{year}/{month}")
    public ResponseEntity<byte[]> downloadMonthlyTransactionPdf(
            @PathVariable int year,
            @PathVariable int month) {

        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Get wallet
        Optional<Wallet> walletOpt = walletService.getWalletByUserId(userInfo.getUserId());
        if (walletOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Wallet wallet = walletOpt.get();

        try {
            // Validate month and year
            if (month < 1 || month > 12 || year < 2000 || year > 2100) {
                return ResponseEntity.badRequest().build();
            }

            // Get transactions for the specified month
            List<Transaction> transactions = transactionService.getTransactionsByMonth(
                    wallet.getId(), year, month);

            // Generate PDF
            byte[] pdfBytes = pdfService.generateMonthlyTransactionPdf(wallet, transactions, year, month);

            // Set up response headers for PDF download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            // Format month name for filename
            String monthName = Month.of(month).toString();
            String filename = "transaction_history_" + year + "_" + monthName + ".pdf";

            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            logger.error("Error generating PDF", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/history")
//    public ResponseEntity<?> getTransactionHistory(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        UserInfo userInfo = getCurrentUserInfo();
//        if (userInfo == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("error", "User not authenticated"));
//        }
//
//        String userId = userInfo.getUserId();
//
//        // Check if user has a wallet
//        Optional<Wallet> walletOpt = walletService.getWalletByUserId(userId);
//        if (walletOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Map.of("error", "No wallet found for user"));
//        }
//
//        String walletId = walletOpt.get().getId();
//
//        // Get paginated transaction history
//        Page<Transaction> transactions = transactionService.getWalletTransactionsPaged(
//                walletId,
//                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"))
//        );
//
//        return ResponseEntity.ok(Map.of(
//                "content", transactions.getContent(),
//                "totalPages", transactions.getTotalPages(),
//                "totalElements", transactions.getTotalElements(),
//                "currentPage", transactions.getNumber()
//        ));
//    }

    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        String userId = userInfo.getUserId();

        // Check if user has a wallet
        Optional<Wallet> walletOpt = walletService.getWalletByUserId(userId);
        if (walletOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No wallet found for user"));
        }

        String walletId = walletOpt.get().getId();

        // Get paginated transaction history
        Page<Transaction> transactions = transactionService.getWalletTransactionsPaged(
                walletId, page, size, sortBy, direction);

        // Build paginated response
        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transactions.getContent());
        response.put("currentPage", transactions.getNumber());
        response.put("totalItems", transactions.getTotalElements());
        response.put("totalPages", transactions.getTotalPages());
        response.put("size", transactions.getSize());
        response.put("first", transactions.isFirst());
        response.put("last", transactions.isLast());
        response.put("empty", transactions.isEmpty());

        return ResponseEntity.ok(response);
    }

    // Add an additional endpoint for date range filtered history with pagination
    @GetMapping("/history/filter")
    public ResponseEntity<?> getFilteredTransactionHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        // Get wallet
        Optional<Wallet> walletOpt = walletService.getWalletByUserId(userInfo.getUserId());
        if (walletOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No wallet found for user"));
        }

        String walletId = walletOpt.get().getId();

        // Build criteria query (you'll need to implement this in the service)
        Page<Transaction> transactions;

        // This is a simplified approach - in a real app, you'd want to build a more sophisticated query
        if (startDate != null && endDate != null && type != null) {
            transactions = transactionService.getFilteredTransactions(
                    walletId, startDate, endDate, type, page, size, sortBy, direction);
        } else {
            transactions = transactionService.getWalletTransactionsPaged(
                    walletId, page, size, sortBy, direction);
        }

        // Build response with pagination metadata
        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transactions.getContent());
        response.put("currentPage", transactions.getNumber());
        response.put("totalItems", transactions.getTotalElements());
        response.put("totalPages", transactions.getTotalPages());
        response.put("size", transactions.getSize());

        return ResponseEntity.ok(response);
    }




    private UserInfo getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return new UserInfo(userDetails.getUserId(), userDetails.getUsername());
        }
        return null;
    }
}