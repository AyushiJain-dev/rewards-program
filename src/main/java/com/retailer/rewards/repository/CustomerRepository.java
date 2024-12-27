package com.retailer.rewards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retailer.rewards.model.Customer;

/**
 * Repository interface for managing Customer entities.
 * 
 * This interface extends JpaRepository to provide CRUD operations for the
 * Customer entity. It is automatically implemented by Spring Data JPA at
 * runtime.
 * 
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
