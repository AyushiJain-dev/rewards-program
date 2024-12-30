package com.retailer.rewards.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.RewardSummaryResponse;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.service.RewardService;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller class to handle requests for customers and transactions.
 */
@RestController
@RequestMapping("/api/rewards")
@Slf4j
public class RewardController {

	@Autowired
	private RewardService rewardService;

	/**
	 * Creates a new customer with the given name.
	 * 
	 * @param name The name of the customer to be created.
	 * @return The created Customer object.
	 */
	@PostMapping("/create-customer")
	public Customer createCustomer(@RequestParam String name) {
		log.info("Creating customer with name: {}", name);
		Customer customer = rewardService.createCustomer(name);
		log.info("Customer created successfully with ID: {}", customer.getId());
		return customer;
	}

	/**
	 * Creates a transaction for the specified customer with the given amount and
	 * date.
	 * 
	 * @param customerId The unique ID of the customer for whom the transaction is
	 *                   being created.
	 * @param amount     The amount of the transaction.
	 * @param date       The date of the transaction, it will take current date if
	 *                   date not given.
	 * @return The created Transaction object.
	 */
	@PostMapping("/create-transaction")
	public Transaction createTransaction(@RequestParam Long customerId, @RequestParam double amount,
			@RequestParam(required = false) LocalDate date) {
		log.info("Creating transaction for customer ID: {} with amount: {} and date: {}", customerId, amount, date);
		Transaction transaction = rewardService.createTransaction(customerId, amount, date);
		log.info("Transaction created successfully with ID: {} for Customer: {} on date: {}", transaction.getId(),
				transaction.getCustomer().getName(), transaction.getDate());
		return transaction;
	}

	/**
	 * Fetches the details of a customer by their ID.
	 * 
	 * @param customerId The unique ID of the customer.
	 * @return The Customer object containing details of the requested customer.
	 */
	@GetMapping("/customers/{customerId}")
	public Customer getCustomer(@PathVariable Long customerId) {
		log.info("Fetching customer details for customer ID: {}", customerId);
		return rewardService.getCustomer(customerId);
	}

	/**
	 * Retrieves a summary of reward points for a given customer within a specified
	 * date range.
	 * 
	 * @param customerId the unique ID of the customer for whom the transaction is
	 *                   being created.
	 * @param startDate  the start date of the period to consider for transactions
	 * @param endDate    the end date of the period to consider for transactions
	 * @return a RewardSummaryResponse containing the customer's details,
	 *         transactions, reward points per month, and the total reward points
	 *         earned in the specified period
	 */
	@GetMapping("reward-summary/{customerId}")
	public ResponseEntity<RewardSummaryResponse> getRewardsSummary(@PathVariable Long customerId,
			@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
		log.info("Fetching rewards summary for customer ID: {} between period {} and {}", customerId, startDate,
				endDate);
		RewardSummaryResponse response = rewardService.getRewardsSummary(customerId, startDate, endDate);
		return ResponseEntity.ok(response);
	}
}
