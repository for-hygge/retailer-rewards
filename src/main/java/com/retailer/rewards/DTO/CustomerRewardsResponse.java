package com.retailer.rewards.DTO;

import java.util.List;

public record CustomerRewardsResponse (
    Long id,
    String name,
    Integer totalPurchase,
    Integer totalRewards,
    List<MonthlyTransaction> transactions
) {}
