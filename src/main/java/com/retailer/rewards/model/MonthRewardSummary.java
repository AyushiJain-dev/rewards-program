package com.retailer.rewards.model;

import java.time.Month;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This model holds monthly reward summary.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MonthRewardSummary {
	private int year;
	private Month month;
	private int points;
}
