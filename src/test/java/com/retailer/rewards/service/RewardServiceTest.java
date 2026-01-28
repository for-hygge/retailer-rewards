package com.retailer.rewards.service;

import com.retailer.rewards.DTO.CustomerRewardsResponse;
import com.retailer.rewards.DTO.UpdateRewards;
import com.retailer.rewards.Entity.Customer;
import com.retailer.rewards.Entity.Transaction;
import com.retailer.rewards.exception.CustomerNotFound;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.util.Helper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    Helper helper;

    @InjectMocks
    RewardService rewardService;

    @Test
    void updateRewards_shouldThrow_ifMonthOutOfRange() {
        UpdateRewards req = new UpdateRewards();
        req.setTransactionMonth(13);
        req.setPurchase(100);

        assertThatThrownBy(() -> rewardService.updateRewards(req, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Month must be 1-12");

        verifyNoInteractions(customerRepository, helper);
    }

    @Test
    void updateRewards_shouldThrow_ifPurchaseNegative() {
        UpdateRewards req = new UpdateRewards();
        req.setTransactionMonth(5);
        req.setPurchase(-1);

        assertThatThrownBy(() -> rewardService.updateRewards(req, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Purchase must be >= 0");

        verifyNoInteractions(customerRepository, helper);
    }

    @Test
    void updateRewards_shouldThrowCustomerNotFound_ifCustomerMissing() {
        Long id = 99L;

        UpdateRewards req = new UpdateRewards();
        req.setTransactionMonth(2);
        req.setPurchase(50);

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rewardService.updateRewards(req, id))
                .isInstanceOf(CustomerNotFound.class)
                .hasMessageContaining("Customer not found, id: " + id);

        verify(customerRepository).findById(id);
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(helper);
    }

    @Test
    void updateRewards_shouldAddTransaction_updateTotals_save_andReturnResponse() {
        Long id = 1L;

        UpdateRewards req = new UpdateRewards();
        req.setTransactionMonth(7);
        req.setPurchase(120);

        Customer customer = new Customer("Alice", 200, 50);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(helper.calculateRewards(120)).thenReturn(90);

        when(customerRepository.findWithTransactionsById(id)).thenReturn(Optional.of(customer));

        CustomerRewardsResponse expected =
                new CustomerRewardsResponse(id, "Alice", 320, 140, List.of());
        when(helper.toCustomerRewardsResponse(customer)).thenReturn(expected);

        CustomerRewardsResponse result = rewardService.updateRewards(req, id);

        assertThat(result).isSameAs(expected);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());
        Customer savedArg = captor.getValue();

        assertThat(savedArg.getTotalPurchase()).isEqualTo(200 + 120);
        assertThat(savedArg.getTotalRewards()).isEqualTo(50 + 90);

        assertThat(savedArg.getTransactions()).hasSize(1);
        Transaction tx = savedArg.getTransactions().get(0);
        assertThat(tx.getTransactionMonth()).isEqualTo(7);
        assertThat(tx.getPurchase()).isEqualTo(120);
        assertThat(tx.getAddedPoints()).isEqualTo(90);
        assertThat(tx.getCustomer()).isSameAs(savedArg);

        verify(customerRepository).findById(id);
        verify(helper).calculateRewards(120);
        verify(customerRepository).findWithTransactionsById(id);
        verify(helper).toCustomerRewardsResponse(customer);
        verifyNoMoreInteractions(customerRepository, helper);
    }
}
