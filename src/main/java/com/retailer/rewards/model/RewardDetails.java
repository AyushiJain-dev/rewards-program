package com.retailer.rewards.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This model holds reward details
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RewardDetails {
	private String month;
	private int totalAmount;
	private int rewardPoints;
}
