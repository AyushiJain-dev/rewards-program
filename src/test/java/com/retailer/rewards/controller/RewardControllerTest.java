package com.retailer.rewards.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

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
import com.retailer.rewards.model.MonthRewardSummary;
import com.retailer.rewards.model.RewardSummaryResponse;
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
	void testGetRewardsSummary() throws Exception {
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate endDate = LocalDate.of(2024, 1, 31);

		Transaction t1 = new Transaction(1L, 120.0, LocalDate.of(2024, 1, 10), customer);
		Transaction t2 = new Transaction(2L, 220.0, LocalDate.of(2024, 1, 15), customer);
		List<Transaction> transactions = Arrays.asList(t1, t2);

		MonthRewardSummary m1 = new MonthRewardSummary(2024, Month.JANUARY, 380);
		List<MonthRewardSummary> monthRewardSummary = Arrays.asList(m1);

		when(rewardService.getRewardsSummary(1L, startDate, endDate))
				.thenReturn(new RewardSummaryResponse(1L, "Test Name", transactions, monthRewardSummary, 380));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards/reward-summary/{customerId}", 1L)
				.param("startDate", startDate.toString()).param("endDate", endDate.toString()))
				.andExpect(status().isOk()).andExpect(jsonPath("$.customerId").value(1L))
				.andExpect(jsonPath("$.customerName").value("Test Name"))
				.andExpect(jsonPath("$.totalRewardPoints").value(380))
				.andExpect(jsonPath("$.rewardPointsPerMonth[0].year").value(2024))
	            .andExpect(jsonPath("$.rewardPointsPerMonth[0].month").value("JANUARY"))
	            .andExpect(jsonPath("$.rewardPointsPerMonth[0].points").value(380));

		verify(rewardService, times(1)).getRewardsSummary(1L, startDate, endDate);
	}
}
