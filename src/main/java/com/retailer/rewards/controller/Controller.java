package com.retailer.rewards.controller;

import com.retailer.rewards.DTO.CreateCustomer;
import com.retailer.rewards.DTO.CustomerRewardsResponse;
import com.retailer.rewards.DTO.UpdateRewards;
import com.retailer.rewards.Entity.Customer;
import com.retailer.rewards.service.CustomerService;
import com.retailer.rewards.service.RewardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/retailer-rewards")
public class Controller {
    private final CustomerService customerService;
    private final RewardService rewardService;

    public Controller(CustomerService customerService, RewardService rewardService) {
        this.customerService = customerService;
        this.rewardService = rewardService;
    }

    @GetMapping("/customers")
    public List<CustomerRewardsResponse> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/customer")
    public Customer createCustomer(@RequestBody CreateCustomer customer) {
        return customerService.createCustomer(customer);
    }

    @PostMapping("/rewards/{id}")
    public CustomerRewardsResponse updateRewards(@RequestBody UpdateRewards updateRewards,
                                                 @PathVariable Long id) {
        return rewardService.updateRewards(updateRewards, id);
    }
}
