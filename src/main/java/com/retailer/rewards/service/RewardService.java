package com.retailer.rewards.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retailer.rewards.RewardCalculator;
import com.retailer.rewards.exceptionHandler.CustomerNotFoundException;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.RewardSummaryResponse;
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
	 * Retrieves a summary of reward points for a given customer within a specified
	 * date range. The summary includes the total reward points, the transactions
	 * made during the period, and the reward points broken down by month.
	 *
	 * @param customerId the unique ID of the customer
	 * @param startDate  the start date of the period to consider for transactions
	 * @param endDate    the end date of the period to consider for transactions
	 * @return a RewardSummaryResponse containing the customer's details,
	 *         transactions, reward points per month, and the total reward points
	 *         earned in the specified period
	 * @throws IllegalArgumentException if the endDate is before the startDate
	 */
	public RewardSummaryResponse getRewardsSummary(Long customerId, LocalDate startDate, LocalDate endDate) {

		// Validate that the endDate is after startDate
		if (endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("End date cannot be before start date.");
		}

		// Check if customer exists
		Customer customer = findCustomerById(customerId);

		List<Transaction> transactions = transactionRepository.findTransactionsByCustomerIdAndDateBetween(customerId,
				startDate, endDate);

		// Group transactions by month and calculate reward points per month
		Map<Month, Integer> rewardPointsPerMonth = transactions.stream().filter(
				transaction -> !transaction.getDate().isBefore(startDate) && !transaction.getDate().isAfter(endDate))
				.collect(Collectors.groupingBy(transaction -> transaction.getDate().getMonth(), TreeMap::new, Collectors
						.summingInt(transaction -> RewardCalculator.calculateRewardPoints(transaction.getAmount()))));

		Set<Month> monthsInRange = monthsBetween(startDate, endDate);

		// Ensure that all months in the range have an entry in the rewardPointsPerMonth map
		for (Month month : monthsInRange) {
			rewardPointsPerMonth.putIfAbsent(month, 0);
		}

		// Calculate total reward points across all transactions
		int totalPoints = rewardPointsPerMonth.values().stream().mapToInt(Integer::intValue).sum();

		return new RewardSummaryResponse(customer.getId(), customer.getName(), transactions, rewardPointsPerMonth,
				totalPoints);
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

	/**
	 * Method to get all months between startDate and endDate
	 *
	 * @param startDate the start date of the period.
	 * @param endDate   the end date of the period.
	 * @return months between the given date range.
	 */
	private Set<Month> monthsBetween(LocalDate startDate, LocalDate endDate) {
		Set<Month> months = new HashSet<>();
		LocalDate currentDate = startDate;

		// Add months from startDate to endDate
		while (!currentDate.isAfter(endDate)) {
			months.add(currentDate.getMonth());
			currentDate = currentDate.plusMonths(1);
		}

		return months;
	}
}
