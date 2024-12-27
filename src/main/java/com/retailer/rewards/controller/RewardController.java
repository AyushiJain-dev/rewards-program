package com.retailer.rewards.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retailer.rewards.model.Customer;
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
	 * Fetches the total rewards for a specified customer based on all transactions.
	 * 
	 * @param customerId The unique ID of the customer.
	 * @return The total reward points of the customer.
	 */
	@GetMapping("/total-rewards/{customerId}")
	public int getTotalRewards(@PathVariable Long customerId) {
		log.info("Fetching total rewards for customer ID: {}", customerId);
		int totalRewards = rewardService.getTotalRewards(customerId);
		log.info("Total rewards for customer ID {}: {}", customerId, totalRewards);
		return totalRewards;
	}

	/**
	 * Fetches the monthly rewards for a specified customer based on a specific
	 * month and year.
	 * 
	 * @param customerId The unique ID of the customer.
	 * @param month      The month for which the rewards are to be calculated.
	 * @param year       The year for which the rewards are to be calculated.
	 * @return The reward points earned in the specified month.
	 */
	@GetMapping("/monthly-rewards/{customerId}")
	public int getMonthlyRewards(@PathVariable Long customerId, @RequestParam int month, @RequestParam int year) {
		log.info("Fetching monthly rewards for customer ID: {} for month: {} and year: {}", customerId, month, year);
		int monthlyRewards = rewardService.getMonthlyRewards(customerId, month, year);
		log.info("Monthly rewards for customer ID {} in {}/{}: {}", customerId, month, year, monthlyRewards);
		return monthlyRewards;
	}
}
