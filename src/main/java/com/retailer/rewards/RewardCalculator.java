package com.retailer.rewards;

/**
 * A class to calculate reward points for a given purchase amount.
 */
public class RewardCalculator {

	/**
     * Calculates the reward points based on the purchase amount.
     * 
     * Reward points are calculated based on the following criteria:
     * - For amounts over $100, 2 points are awarded for every dollar above $100.
     * - For amounts between $50 and $100, 1 point is awarded for every dollar above $50.
     * 
     * @param amount The total purchase amount.
     * @return The calculated reward points.
     */
	public static int calculateRewardPoints(double amount) {
		int points = 0;
		if (amount > 100) {
			points += (int) ((amount - 100) * 2);
			amount = 100;
		}
		if (amount > 50) {
			points += (int) ((amount - 50) * 1);
		}
		return points;
	}

}
