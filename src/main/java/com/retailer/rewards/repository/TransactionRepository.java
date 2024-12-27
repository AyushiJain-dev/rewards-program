package com.retailer.rewards.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retailer.rewards.model.Transaction;

/**
 * Repository interface for managing Transaction entities.
 * 
 * This interface extends JpaRepository to provide CRUD operations for the
 * Transaction entity. It is automatically implemented by Spring Data JPA at
 * runtime.
 * 
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	/**
	 * Fetches all transactions for a given customer within a specified date range.
	 *
	 * @param customerId The ID of the customer whose transactions are to be
	 *                   retrieved.
	 * @param startDate  The start date of the date range.
	 * @param endDate    The end date of the date range.
	 * @return A list of transactions made by the customer within the specified date
	 *         range.
	 */
	List<Transaction> findByCustomerIdAndDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);

	/**
	 * Fetches all transactions for a given customer regardless of date.
	 *
	 * @param customerId The ID of the customer whose transactions are to be
	 *                   retrieved.
	 * @return A list of all transactions made by the specified customer.
	 */
	List<Transaction> findByCustomerId(Long customerId);

}
