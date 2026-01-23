package com.retailer.rewards.DTO;

import java.util.List;

public record MonthlyTransaction (
        Integer transactionMonth,
        List<TransactionDetail> details,
        Integer rewardsPerMonth
){}
