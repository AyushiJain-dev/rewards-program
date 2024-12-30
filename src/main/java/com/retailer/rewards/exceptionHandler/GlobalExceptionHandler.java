package com.retailer.rewards.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception handler class to handle all exception thrown by the application.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles MissingServletRequestParameterException thrown by the application.
	 * 
	 * @param ex the exception that was thrown, providing the message and details
	 * @return a ResponseEntity containing an ErrorResponse with status BAD_REQUEST
	 *         (400)
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
		log.error("Missing required parameter: {}", ex.getParameterName());
		ErrorResponse errorResponse = new ErrorResponse("Bad Request",
				"Missing required parameter: " + ex.getParameterName());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles MethodArgumentTypeMismatchException thrown by the application.
	 * 
	 * @param ex the exception that was thrown, providing the message and details
	 * @return a ResponseEntity containing an ErrorResponse with status BAD_REQUEST
	 *         (400)
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex) {
		String errorMessage;
		if ("date".equals(ex.getName()) || "startDate".equals(ex.getName()) || "endDate".equals(ex.getName())) {
			log.error("Date value should be in yyyy-mm-dd format.");
			errorMessage = "Date value should be in yyyy-mm-dd format.";
		} else {
			log.error(ex.getName() + " is required. Its value can not be null or empty.");
			errorMessage = ex.getName() + " is required. Its value can not be null or empty.";
		}
		ErrorResponse errorResponse = new ErrorResponse("Bad Request", errorMessage);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles NoResourceFoundException thrown by the application.
	 * 
	 * @param ex the exception that was thrown, providing the message and details
	 * @return a ResponseEntity containing an ErrorResponse with status BAD_REQUEST
	 *         (400)
	 */
	@ExceptionHandler(NoResourceFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
		log.error("Invalid Request.");
		ErrorResponse errorResponse = new ErrorResponse("Bad Request", "Invalid Request.");
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles IllegalArgumentException thrown by the application.
	 * 
	 * @param ex the exception that was thrown, providing the message and details
	 * @return a ResponseEntity containing an ErrorResponse with status BAD_REQUEST
	 *         (400)
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		log.error("Bad Request: {}", ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse("Bad Request", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles CustomerNotFoundException thrown when a customer is not found in the
	 * system.
	 * 
	 * @param ex the exception that was thrown, providing the message and details
	 * @return a ResponseEntity containing an ErrorResponse with status NOT_FOUND
	 *         (404)
	 */
	@ExceptionHandler(CustomerNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
		log.error("Customer Not Found: {}", ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse("Not Found", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles all other exceptions that do not have a specific handler.
	 * 
	 * @param ex the exception that was thrown, providing the message and details
	 * @return a ResponseEntity containing an ErrorResponse with status
	 *         INTERNAL_SERVER_ERROR (500)
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
		log.error("Unexpected Error: {}", ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", "An unexpected error occurred.");
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
