package com.retailer.rewards.model;

import java.time.Month;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This model holds response of reward summary including customer id, customer
 * name, total transactions done by customer, rewards per month and total reward
 * points.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RewardSummaryResponse {
	private Long customerId;
	private String customerName;
	private List<Transaction> transactions;
	private Map<Month, Integer> rewardPointsPerMonth;
	private int totalRewardPoints;
}
