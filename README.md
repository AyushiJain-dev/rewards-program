# Rewards Program API

An API that calculates and tracks customer reward points based on purchase transactions. Points are awarded as follows:

- **2 points per dollar** for amounts over $100.
- **1 point per dollar** for amounts between $50 and $100.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [API Endpoints](#api-endpoints)
- [Running the Project](#running-the-project)

## Overview

This API calculates reward points for customers based on their transaction amounts. The points are awarded according to the following logic:

- For purchases over **$100**, customers earn **2 points per dollar** for the amount over $100.
- For purchases between **$50 and $100**, customers earn **1 point per dollar** for the amount between $50 and $100.

Example Calculation

For a $120 purchase:
- **$20** over $100 earns **2 points per dollar**: `2 * $20 = 40 points`.
- **$50** between $50 and $100 earns **1 point per dollar**: `1 * $50 = 50 points`.

Total reward points: **90 points**.

## Architecture

The API is structured in a layered architecture:

- **Controller Layer**: Handles incoming HTTP requests and provides appropriate responses.
- **Service Layer**: Contains the business logic to calculate reward points.
- **Repository Layer**: Manages the storage of transaction records.
- **POJOs (Plain Old Java Objects)**: Data models for customers and their transactions.
- **Global Exception Handler**: Catches and returns consistent error messages.

## Technologies

The following technologies are used to implement the API:

- **Java**: The primary programming language used to implement the business logic.
- **Spring Boot**: Framework used to build the RESTful API.
- **JUnit**: Used for unit testing.
- **H2 Database**: An in-memory database used to store transaction records and calculate points.
- **Postman**: Tool used for testing the API endpoints.
- **Maven**: Build automation tool.

## API Endpoints

### 1. Create a New Customer.

**POST** `/api/rewards/create-customer?name={customerName}`

**Request Example**: 
`/api/rewards/create-customer?name=XYZ`

**Response**: 
 
```json
{
    "id": 1,
    "name": "XYZ",
    "transactions": null
}
```

### 2. Create a new transaction for the customer with the given amount and date(Optional).

**POST** `/api/rewards/create-transaction?customerId={customerId}&amount={amount}&date={date}`

**Request Example**: 
`/api/rewards/create-transaction?customerId=1&amount=120&date=2024-12-30`

**Response**:

```json
{
    "id": 1,
    "amount": 120.0,
    "date": "2024-12-30"
}
```

**Request Example**: 
`/api/rewards/create-transaction?customerId=1&amount=120`

**Response**:

```json
{
    "id": 1,
    "amount": 120.0,
    "date": {currentDate}
}
```

### 3. Fetch the customer details by their ID.

**GET** `/api/rewards/customers/{customerId}`

**Request Example**: 
`/api/rewards/customers/1`

**Response**:

```json
{
    "id": 1,
    "name": "XYZ",
    "transactions": [
        {
            "id": 1,
            "amount": 120.0,
            "date": "2024-12-30"
        }
    ]
}
```

### 4. Fetch the reward summary for a customer within a specified date range.

**GET** `/api/rewards/reward-summary/{customerId}?startDate={startDate}&endDate={endDate}`

**Request Example**: 
`/api/rewards/reward-summary/1?startDate=2024-10-01&endDate=2024-12-31`

**Response**:

```json
{
    "customerId": 1,
    "customerName": "XYZ",
    "transactions": [
        {
            "id": 1,
            "amount": 120.0,
            "date": "2024-12-30"
        },
        {
            "id": 2,
            "amount": 220.0,
            "date": "2024-11-26"
        }
    ],
    "rewardPointsPerMonth": [
        {
            "year": 2024,
            "month": "OCTOBER",
            "points": 0
        },
        {
            "year": 2024,
            "month": "NOVEMBER",
            "points": 290
        },
        {
            "year": 2024,
            "month": "DECEMBER",
            "points": 90
        }
    ],
    "totalRewardPoints": 380
}
```

## Running the Project

Follow the steps below to clone, build, and run the project:

### 1. Clone the Repository

Clone the project to your local machine using the following command:

```bash
git clone https://github.com/AyushiJain-dev/rewards-program.git
```

### 2. Build the Project

Navigate to the project directory and build the project using Maven. This will download dependencies and compile the project.

```bash
mvn clean install
```

### 3. Run the Application
After building the project, you can run the application with the following command:

```bash
mvn spring-boot:run
```

Once the application is running, you can access the API at [http://localhost:8080](http://localhost:8080)