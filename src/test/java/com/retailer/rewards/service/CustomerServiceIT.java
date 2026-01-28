package com.retailer.rewards.service;

import com.retailer.rewards.DTO.CustomerRewardsResponse;
import com.retailer.rewards.Entity.Customer;
import com.retailer.rewards.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CustomerServiceIT {

    @Autowired CustomerService customerService;
    @Autowired CustomerRepository customerRepository;

    @Test
    void getAllCustomers_shouldReturnResponsesForSavedCustomers() {
        Customer c1 = customerRepository.save(new Customer("A", 0, 0));
        Customer c2 = customerRepository.save(new Customer("B", 10, 5));

        List<CustomerRewardsResponse> result = customerService.getAllCustomers();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(CustomerRewardsResponse::id)
                .containsExactlyInAnyOrder(c1.getId(), c2.getId());
        assertThat(result).extracting(CustomerRewardsResponse::name)
                .containsExactlyInAnyOrder("A", "B");
    }
}
