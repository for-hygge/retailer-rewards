package com.retailer.rewards.service;

import com.retailer.rewards.DTO.CustomerRewardsResponse;
import com.retailer.rewards.DTO.UpdateRewards;
import com.retailer.rewards.Entity.Customer;
import com.retailer.rewards.Entity.Transaction;
import com.retailer.rewards.exception.CustomerNotFound;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.util.Helper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RewardService {
    private final CustomerRepository customerRepository;
    private final Helper helper;

    public RewardService(CustomerRepository customerRepository, Helper helper) {
        this.customerRepository = customerRepository;
        this.helper = helper;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public CustomerRewardsResponse updateRewards(UpdateRewards updateRewards, Long id) {
        int month = updateRewards.getTransactionMonth();
        int purchase = updateRewards.getPurchase();
        if (month < 1 || month > 12) throw new IllegalArgumentException("Month must be 1-12");
        if (purchase < 0) throw new IllegalArgumentException("Purchase must be >= 0");

        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new CustomerNotFound("Customer not found, id: " + id));
        int addRewards = helper.calculateRewards(purchase);

        Transaction transaction = new Transaction();
        transaction.setTransactionMonth(month);
        transaction.setPurchase(purchase);
        transaction.setAddedPoints(addRewards);

        customer.addTransaction(transaction);
        customer.setTotalPurchase(customer.getTotalPurchase() + purchase);
        customer.setTotalRewards(customer.getTotalRewards() + addRewards);
        customerRepository.save(customer);

        Customer saved = customerRepository.findWithTransactionsById(id)
                .orElseThrow(() -> new CustomerNotFound("Customer not found, id: " + id));

        return helper.toCustomerRewardsResponse(saved);
    }
}
