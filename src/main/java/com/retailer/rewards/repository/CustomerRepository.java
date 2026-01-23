package com.retailer.rewards.repository;

import com.retailer.rewards.Entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @EntityGraph(attributePaths = "transactions")
    Optional<Customer> findWithTransactionsById(Long id);
}
