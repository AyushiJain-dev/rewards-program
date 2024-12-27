package com.retailer.rewards.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.service.RewardService;

@ExtendWith(MockitoExtension.class)
public class RewardControllerTest {

	@InjectMocks
	private RewardController rewardController;

	@Mock
	private RewardService rewardService;

	private MockMvc mockMvc;

	private Customer customer;
	private Transaction transaction;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(rewardController).build();
		customer = new Customer(1L, "Test Name", Arrays.asList(transaction));
		transaction = new Transaction(1L, 120.0, LocalDate.now(), customer);
	}

	@Test
	void testCreateCustomer() throws Exception {
		when(rewardService.createCustomer("Test Name")).thenReturn(customer);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/rewards/create-customer").param("name", "Test Name"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(customer.getId()))
				.andExpect(jsonPath("$.name").value(customer.getName()));

		verify(rewardService, times(1)).createCustomer("Test Name");
	}

	@Test
	void testGetCustomer() throws Exception {
		when(rewardService.getCustomer(1L)).thenReturn(customer);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards/customers/{customerId}", 1L))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(customer.getId()))
				.andExpect(jsonPath("$.name").value(customer.getName()));

		verify(rewardService, times(1)).getCustomer(1L);
	}

	@Test
	void testCreateTransaction() throws Exception {
		when(rewardService.createTransaction(1L, 120.0, null)).thenReturn(transaction);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/rewards/create-transaction")
				.param("customerId", String.valueOf(1L)).param("amount", String.valueOf(120.0)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(transaction.getId()))
				.andExpect(jsonPath("$.amount").value(transaction.getAmount()));

		verify(rewardService, times(1)).createTransaction(1L, 120.0, null);
	}

	@Test
	void testGetTotalRewards() throws Exception {
		when(rewardService.getTotalRewards(1L)).thenReturn(90);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards/total-rewards/{customerId}", 1L))
				.andExpect(status().isOk()).andExpect(jsonPath("$").value(90));

		verify(rewardService, times(1)).getTotalRewards(1L);
	}

	@Test
	void testGetMonthlyRewards() throws Exception {
		when(rewardService.getMonthlyRewards(1L, 12, 2024)).thenReturn(90);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards/monthly-rewards/{customerId}", 1L)
				.param("month", String.valueOf(12)).param("year", String.valueOf(2024))).andExpect(status().isOk())
				.andExpect(jsonPath("$").value(90));

		verify(rewardService, times(1)).getMonthlyRewards(1L, 12, 2024);
	}

}
