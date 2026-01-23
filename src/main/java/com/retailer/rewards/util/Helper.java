package com.retailer.rewards.util;

import com.retailer.rewards.DTO.CustomerRewardsResponse;
import com.retailer.rewards.DTO.MonthlyTransaction;
import com.retailer.rewards.DTO.TransactionDetail;
import com.retailer.rewards.Entity.Customer;
import com.retailer.rewards.Entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Helper {
    public CustomerRewardsResponse toCustomerRewardsResponse(Customer customer) {

        Map<Integer, List<Transaction>> grouped = customer.getTransactions().stream()
                .collect(Collectors.groupingBy(Transaction::getTransactionMonth));

        List<MonthlyTransaction> monthly = grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    int month = entry.getKey();
                    List<Transaction> txs = entry.getValue();

                    List<TransactionDetail> details = txs.stream()
                            .sorted(Comparator.comparing(Transaction::getId))
                            .map(t -> new TransactionDetail(
                                    t.getId(),
                                    t.getPurchase(),
                                    t.getAddedPoints()
                            ))
                            .toList();

                    int rewardsPerMonth = txs.stream()
                            .mapToInt(t -> t.getAddedPoints() == null ? 0 : t.getAddedPoints())
                            .sum();

                    return new MonthlyTransaction(month, details, rewardsPerMonth);
                })
                .toList();

        return new CustomerRewardsResponse(
                customer.getId(),
                customer.getName(),
                customer.getTotalPurchase(),
                customer.getTotalRewards(),
                monthly
        );
    }

    public int calculateRewards(int purchase) {
        int rewards = 0;
        if (purchase > 100) {
            rewards += (purchase - 100) * 2;
        }
        if (purchase > 50) {
            rewards += Math.min(purchase, 100) - 50;
        }
        return rewards;
    }
}
