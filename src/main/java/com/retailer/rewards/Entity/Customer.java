package com.retailer.rewards.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "customers")
@NoArgsConstructor
public class Customer {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @Column(nullable = false)
    private Integer totalPurchase;

    @Column(nullable = false)
    private Integer totalRewards;

    public Customer(String name, Integer totalPurchase, Integer totalRewards) {
        this.name = name;
        this.totalPurchase = totalPurchase;
        this.totalRewards = totalRewards;
    }

    public void setTotalPurchase(Integer totalPurchase) {
        if (totalPurchase < 0) {
            throw new IllegalArgumentException("Purchase must be >= 0");
        }
        this.totalPurchase = totalPurchase;
    }

    public void setTotalRewards(@NonNull Integer totalRewards) {
        if (totalRewards < 0) {
            throw new IllegalArgumentException("Rewards must be >= 0");
        }
        this.totalRewards = totalRewards;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setCustomer(this);
    }
}
