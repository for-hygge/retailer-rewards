package com.retailer.rewards.service;

import com.retailer.rewards.DTO.CreateCustomer;
import com.retailer.rewards.DTO.CustomerRewardsResponse;
import com.retailer.rewards.Entity.Customer;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.util.Helper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    Helper helper;

    @InjectMocks
    CustomerService customerService;

    @Test
    void getAllCustomers() {
        Customer c1 = new Customer("A", 0, 0);
        Customer c2 = new Customer("B", 10, 5);

        CustomerRewardsResponse r1 =
                new CustomerRewardsResponse(1L, "A", 0, 0, List.of());
        CustomerRewardsResponse r2 =
                new CustomerRewardsResponse(2L, "B", 10, 5, List.of());
        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));
        when(helper.toCustomerRewardsResponse(c1)).thenReturn(r1);
        when(helper.toCustomerRewardsResponse(c2)).thenReturn(r2);

        List<CustomerRewardsResponse> result = customerService.getAllCustomers();

        assertThat(result).containsExactly(r1, r2);
        verify(customerRepository).findAll();
        verify(helper).toCustomerRewardsResponse(c1);
        verify(helper).toCustomerRewardsResponse(c2);
        verifyNoMoreInteractions(customerRepository, helper);
    }

    @Test
    void createCustomer() {
        CreateCustomer req = new CreateCustomer();
        req.setName("John");

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(inv -> inv.getArgument(0, Customer.class));

        Customer saved = customerService.createCustomer(req);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());
        Customer toSave = captor.getValue();

        assertEquals("John", toSave.getName());
        assertEquals(0, toSave.getTotalPurchase());
        assertEquals(0, toSave.getTotalRewards());

        assertEquals("John", saved.getName());
        assertEquals(0, saved.getTotalPurchase());
        assertEquals(0, saved.getTotalRewards());

        verifyNoMoreInteractions(customerRepository, helper);
    }
}