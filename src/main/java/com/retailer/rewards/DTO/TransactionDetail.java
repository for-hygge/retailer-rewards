package com.retailer.rewards.DTO;

public record TransactionDetail(
        Long id,
        Integer purchase,
        Integer addPoints
) {}
