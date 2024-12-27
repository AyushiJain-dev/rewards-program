package com.retailer.rewards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
	void testCreateCustomerWithNullName() {
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
	void testGetCustomerNotFound() {
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
	void testGetTotalRewards() {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		when(transactionRepository.findByCustomerId(1L)).thenReturn(List.of(transaction));

		int totalRewards = rewardService.getTotalRewards(1L);

		assertEquals(90, totalRewards);
	}

	@Test
	void testGetMonthlyRewards() {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		when(transactionRepository.findByCustomerIdAndDateBetween(1L, LocalDate.of(2024, 12, 1),
				LocalDate.of(2024, 12, 31))).thenReturn(List.of(transaction));

		int monthlyRewards = rewardService.getMonthlyRewards(1L, 12, 2024);

		assertEquals(90, monthlyRewards);
	}

	@Test
	void testGetMonthlyRewardsWithNoTransactions() {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		when(transactionRepository.findByCustomerIdAndDateBetween(1L, LocalDate.of(2024, 11, 1),
				LocalDate.of(2024, 11, 30))).thenReturn(List.of());

		int monthlyRewards = rewardService.getMonthlyRewards(1L, 11, 2024);

		assertEquals(0, monthlyRewards);
	}

	@Test
	void testGetTotalRewardsCustomerNotFound() {
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class,
				() -> rewardService.getTotalRewards(1L));
		assertEquals("Customer not found with ID: 1", exception.getMessage());
	}

	@Test
	void testCreateTransactionCustomerNotFound() {
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class,
				() -> rewardService.createTransaction(1L, 120.0, LocalDate.now()));
		assertEquals("Customer not found with ID: 1", exception.getMessage());
	}
}
