package com.retailer.rewards.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * ErrorResponse represents the structure of the error response that will be
 * returned in case of an exception in the system.
 */
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
	private String status;
	private String message;
}
