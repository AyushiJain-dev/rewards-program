package com.retailer.rewards.exceptionHandler;

/**
 * A class to handle customerNotFoundException thrown by the application.
 */
public class CustomerNotFoundException extends RuntimeException {
	public CustomerNotFoundException(String message) {
		super(message);
	}
}
