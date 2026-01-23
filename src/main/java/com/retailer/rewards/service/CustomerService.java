package com.retailer.rewards.service;

import com.retailer.rewards.DTO.CreateCustomer;
import com.retailer.rewards.DTO.CustomerRewardsResponse;
import com.retailer.rewards.Entity.Customer;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.util.Helper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final Helper helper;

    public CustomerService(CustomerRepository customerRepository, Helper helper) {
        this.customerRepository = customerRepository;
        this.helper = helper;
    }

    @Transactional(readOnly = true)
    public List<CustomerRewardsResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(helper::toCustomerRewardsResponse)
                .toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Customer createCustomer(CreateCustomer newCustomer) {
        Customer customer = new Customer();
        customer.setName(newCustomer.getName());
        customer.setTotalPurchase(0);
        customer.setTotalRewards(0);
        return customerRepository.save(customer);
    }
}
