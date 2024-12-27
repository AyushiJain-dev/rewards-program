#Rewards Program API

An API that calculates and tracks customer reward points based on purchase transactions. Points are awarded as follows:

- 2 points per dollar for amounts over $100.
- 1 point per dollar for amounts between $50 and $100.

#Table of Contents

- Overview
- Architecture
- Technologies
- API Endpoints
- Running the Project

#Overview

This API calculates reward points for customers based on their transaction amounts:

- Example: A $120 purchase = 2*$20 + 1*$50 = 90 points (2 points for $20 over $100, 1 point for $50 between $50 and $100).

#Architecture

- Controller Layer: Handles incoming HTTP requests.
- Service Layer: Contains business logic to calculate points.
- Repository Layer: Manages data storage.
- POJOs: Data models for Customer and their transactions.
- Global Exception Handler: Catches and returns consistent error messages.

#Technologies

- Java: The primary programming language used to implement the business logic.
- Spring Boot: Framework used to develop the RESTful API.
- JUnit: For writing test cases.
- H2 Database: Used to store transaction records and calculate points.
- Postman: Used for testing the API endpoints.
- Maven: Build automation tool.

#API Endpoints

- POST /create-customer?name={customerName} -> Creates a new customer.

- GET /customers/{customerId} -> Fetches the details of a customer by their ID.

- POST /create-transaction?customerId={customerId}&amount={amount}&date={date} -> Creates a transaction for the specified customer with the given amount and date(Optional).

- GET /total-rewards/{customerId} -> Fetches the total rewards for a specified customer based on all transactions.

- GET /monthly-rewards/{customerId}?month={month}&year={year} -> Fetches the monthly rewards for a specified customer based on a specific month and year.

#Running the Project

- Clone the repository: git clone https://github.com/AyushiJain-dev/rewards-program.git

- Build the project: mvn clean install

- Run the application: mvn spring-boot:run

API available at http://localhost:8080.
