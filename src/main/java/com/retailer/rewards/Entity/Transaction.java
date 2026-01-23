package com.retailer.rewards.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer purchase;

    @Column(nullable = false)
    private Integer transactionMonth;

    @Column(nullable = false)
    private Integer addedPoints;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    public Transaction(Integer purchase, Integer transactionMonth, Integer addedPoints, Customer customer) {
        this.purchase = purchase;
        this.transactionMonth = transactionMonth;
        this.addedPoints = addedPoints;
        this.customer = customer;
    }

    public Transaction(Integer transactionMonth, Integer purchase, Integer addedPoints) {
        this.transactionMonth = transactionMonth;
        this.purchase = purchase;
        this.addedPoints = addedPoints;
    }

    public Transaction(Integer purchase, Integer transactionMonth) {
        this.purchase = purchase;
        this.transactionMonth = transactionMonth;
    }
}
