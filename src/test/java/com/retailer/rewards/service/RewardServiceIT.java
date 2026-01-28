package com.retailer.rewards.service;

import com.retailer.rewards.DTO.CustomerRewardsResponse;
import com.retailer.rewards.DTO.UpdateRewards;
import com.retailer.rewards.Entity.Customer;
import com.retailer.rewards.Entity.Transaction;
import com.retailer.rewards.exception.CustomerNotFound;
import com.retailer.rewards.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RewardServiceIT {

    @Autowired RewardService rewardService;
    @Autowired CustomerRepository customerRepository;

    @Test
    void updateRewards_shouldPersistTransactionAndTotals() {
        Customer c = customerRepository.save(new Customer("Alice", 0, 0));

        UpdateRewards req = new UpdateRewards();
        req.setTransactionMonth(3);
        req.setPurchase(120);

        CustomerRewardsResponse resp = rewardService.updateRewards(req, c.getId());
        assertThat(resp.totalPurchase()).isEqualTo(120);
        assertThat(resp.totalRewards()).isEqualTo(90);
        assertThat(resp.transactions()).hasSize(1);
        assertThat(resp.transactions().get(0).transactionMonth()).isEqualTo(3);
        assertThat(resp.transactions().get(0).rewardsPerMonth()).isEqualTo(90);

        Customer fromDb = customerRepository.findById(c.getId()).orElseThrow();
        assertThat(fromDb.getTotalPurchase()).isEqualTo(120);
        assertThat(fromDb.getTotalRewards()).isEqualTo(90);

        Customer withTx = customerRepository.findWithTransactionsById(c.getId()).orElseThrow();
        assertThat(withTx.getTransactions()).hasSize(1);
        Transaction tx = withTx.getTransactions().get(0);
        assertThat(tx.getTransactionMonth()).isEqualTo(3);
        assertThat(tx.getPurchase()).isEqualTo(120);
        assertThat(tx.getAddedPoints()).isEqualTo(90);
        assertThat(tx.getCustomer().getId()).isEqualTo(c.getId());
    }

    @Test
    void updateRewards_shouldThrowCustomerNotFound_whenIdNotExist() {
        UpdateRewards req = new UpdateRewards();
        req.setTransactionMonth(3);
        req.setPurchase(120);

        assertThatThrownBy(() -> rewardService.updateRewards(req, 999999L))
                .isInstanceOf(CustomerNotFound.class);
    }
}
