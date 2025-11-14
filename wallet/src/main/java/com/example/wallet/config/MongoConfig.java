package com.example.wallet.config;

import com.example.wallet.model.Wallet;
import com.example.wallet.repository.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.math.BigDecimal;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.wallet.repository")
public class MongoConfig {

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/walletdb");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDatabaseFactory());
    }

    @Bean
    @Profile("dev") // Only run in dev profile
    public CommandLineRunner initDatabase(WalletRepository walletRepository) {
        return args -> {
            // Check if we already have wallets
            if (walletRepository.count() == 0) {
                System.out.println("Initializing sample wallets...");

                // Create some sample wallets
                Wallet wallet1 = new Wallet();
                wallet1.setUserId("user123");  // Match with your test user ID
                wallet1.setOwner("John Doe");
                wallet1.setBalance(new BigDecimal("1000.00"));
                wallet1.setCurrency("USD");

                Wallet wallet2 = new Wallet();
                wallet2.setUserId("user123");
                wallet2.setOwner("John Doe");
                wallet2.setBalance(new BigDecimal("500.00"));
                wallet2.setCurrency("EUR");

                walletRepository.save(wallet1);
                walletRepository.save(wallet2);

                System.out.println("Sample wallets created!");
            }
        };
    }

}