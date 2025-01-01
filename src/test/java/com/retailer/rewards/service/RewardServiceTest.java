package com.retailer.rewards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.retailer.rewards.exceptionHandler.CustomerNotFoundException;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.MonthRewardSummary;
import com.retailer.rewards.model.RewardSummaryResponse;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private RewardService rewardService;

	private Customer customer;
	private Transaction transaction;

	@BeforeEach
	void setUp() {
		customer = new Customer(1L, "Test Name", Arrays.asList(transaction));
		transaction = new Transaction(1L, 120.0, LocalDate.now(), customer);
	}

	@Test
	void testCreateCustomer() {
		when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

		Customer result = rewardService.createCustomer("Test Name");

		assertNotNull(result);
		assertEquals("Test Name", result.getName());
		verify(customerRepository, times(1)).save(Mockito.any(Customer.class));
	}

	@Test
	void testCreateCustomer_withNullName() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> rewardService.createCustomer(null));
		assertEquals("Customer name is required.", exception.getMessage());
	}

	@Test
	void testGetCustomer() {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

		Customer result = rewardService.getCustomer(1L);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		verify(customerRepository, times(1)).findById(1L);
	}

	@Test
	void testGetCustomer_notFound() {
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class,
				() -> rewardService.getCustomer(1L));
		assertEquals("Customer not found with ID: 1", exception.getMessage());
	}

	@Test
	void testCreateTransaction() {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);

		Transaction result = rewardService.createTransaction(1L, 120.0, null);

		assertNotNull(result);
		assertEquals(120.0, result.getAmount());
		verify(transactionRepository, times(1)).save(Mockito.any(Transaction.class));
	}

	@Test
	void testCreateTransaction_customerNotFound() {
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class,
				() -> rewardService.createTransaction(1L, 120.0, LocalDate.now()));
		assertEquals("Customer not found with ID: 1", exception.getMessage());
	}

	@Test
	void testGetRewardsSummary() {
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate endDate = LocalDate.of(2024, 1, 31);

		Transaction t1 = new Transaction(1L, 120.0, LocalDate.of(2024, 1, 10), customer);
		Transaction t2 = new Transaction(2L, 220.0, LocalDate.of(2024, 1, 15), customer);
		List<Transaction> transactions = Arrays.asList(t1, t2);

		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		when(transactionRepository.findTransactionsByCustomerIdAndDateBetween(1L, startDate, endDate))
				.thenReturn(transactions);

		RewardSummaryResponse response = rewardService.getRewardsSummary(1L, startDate, endDate);

		assertNotNull(response);
		assertEquals(1L, response.getCustomerId());
		assertEquals("Test Name", response.getCustomerName());
		assertEquals(380, response.getTotalRewardPoints());
		assertNotNull(response.getRewardPointsPerMonth());
		assertEquals(1, response.getRewardPointsPerMonth().size());

		MonthRewardSummary monthRewardSummary = response.getRewardPointsPerMonth().get(0);
		assertEquals(2024, monthRewardSummary.getYear());
		assertEquals(Month.JANUARY, monthRewardSummary.getMonth());
		assertEquals(380, monthRewardSummary.getPoints());

		verify(customerRepository, times(1)).findById(1L);
		verify(transactionRepository, times(1)).findTransactionsByCustomerIdAndDateBetween(1L, startDate, endDate);
	}

	@Test
	void testGetRewardsSummary_invalidEndDate() {
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate endDate = LocalDate.of(2023, 12, 31);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			rewardService.getRewardsSummary(1L, startDate, endDate);
		});
		assertEquals("End date cannot be before start date.", exception.getMessage());

		verify(customerRepository, times(0)).findById(1L);
		verify(transactionRepository, times(0)).findTransactionsByCustomerIdAndDateBetween(1L, startDate, endDate);
	}

	@Test
	void testGetRewardsSummary_customerNotFound() {
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate endDate = LocalDate.of(2024, 1, 31);

		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
			rewardService.getRewardsSummary(1L, startDate, endDate);
		});
		assertEquals("Customer not found with ID: 1", exception.getMessage());

		verify(customerRepository, times(1)).findById(1L);
		verify(transactionRepository, times(0)).findTransactionsByCustomerIdAndDateBetween(1L, startDate, endDate);
	}
}
