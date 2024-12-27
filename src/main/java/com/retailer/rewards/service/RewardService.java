package com.retailer.rewards.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retailer.rewards.RewardCalculator;
import com.retailer.rewards.exceptionHandler.CustomerNotFoundException;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;

/**
 * Service class to handle business logic for rewards calculation. This includes
 * customer creation, transaction handling, and rewards calculation.
 */
@Service
public class RewardService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	/**
	 * Creates a new customer with the given name.
	 *
	 * @param name the name of the customer to be created.
	 * @return the newly created customer.
	 */
	public Customer createCustomer(String name) {
		if (name == null || name.trim().isEmpty() || "null".equals(name.trim())) {
			throw new IllegalArgumentException("Customer name is required.");
		}
		Customer customer = new Customer();
		customer.setName(name);
		return customerRepository.save(customer);
	}

	/**
	 * Retrieves a customer by their unique ID.
	 *
	 * @param customerId the ID of the customer to retrieve.
	 * @return the customer with the given ID.
	 */
	public Customer getCustomer(Long customerId) {
		return findCustomerById(customerId);
	}

	/**
	 * Creates a new transaction for a customer.
	 *
	 * @param customerId the ID of the customer making the transaction.
	 * @param amount     the transaction amount.
	 * @param date       the transaction date (defaults to the current date if
	 *                   null).
	 * @return the newly created transaction.
	 */
	public Transaction createTransaction(Long customerId, double amount, LocalDate date) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount must be greater than 0.");
		}
		Customer customer = findCustomerById(customerId);
		Transaction transaction = new Transaction();
		transaction.setCustomer(customer);
		transaction.setAmount(amount);
		transaction.setDate(Optional.ofNullable(date).orElse(LocalDate.now()));
		return transactionRepository.save(transaction);
	}

	/**
	 * Calculates the total reward points for a customer based on their
	 * transactions.
	 *
	 * @param customerId the ID of the customer for whom to calculate reward points.
	 * @return the total reward points accumulated by the customer.
	 */
	public int getTotalRewards(Long customerId) {

		// Check if customer exists
		findCustomerById(customerId);

		// Fetch all transactions for the customer
		List<Transaction> allTransactions = transactionRepository.findByCustomerId(customerId);

		return allTransactions.stream()
				.mapToInt(transaction -> RewardCalculator.calculateRewardPoints(transaction.getAmount())).sum();
	}

	/**
	 * Calculates the reward points for a customer for a specific month and year.
	 *
	 * @param customerId the ID of the customer.
	 * @param month      the month for which to calculate rewards.
	 * @param year       the year for which to calculate rewards.
	 * @return the total reward points for the customer for that month.
	 */
	public int getMonthlyRewards(Long customerId, int month, int year) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("Month must be between 1 and 12.");
		}

		// Check if customer exists
		findCustomerById(customerId);

		// Define start and end dates of the month
		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

		// Fetch transactions for the customer in the given date range
		List<Transaction> monthlyTransactions = transactionRepository.findByCustomerIdAndDateBetween(customerId,
				startDate, endDate);

		return monthlyTransactions.stream()
				.mapToInt(transaction -> RewardCalculator.calculateRewardPoints(transaction.getAmount())).sum();
	}

	/**
	 * Finds a customer by their unique ID.
	 *
	 * @param customerId the ID of the customer to find.
	 * @return the customer with the given ID.
	 * @throws CustomerNotFoundException if no customer is found with the given ID.
	 */
	private Customer findCustomerById(Long customerId) {
		if (customerId == null) {
			throw new IllegalArgumentException("Customer ID cannot be null.");
		}

		return customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
	}
}
