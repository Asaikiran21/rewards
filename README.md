# Rewards API

A simple Spring Boot REST API that calculates customer reward points based on transactions.

## How to Run

1. Make sure you have Java 1.8 and Maven 3.9.3 installed.
2. Clone the repository.
3. Run the app using:

```bash
mvn spring-boot:run
```

## Endpoints

- `GET /rewards` - Get rewards for all customers.
- `GET /rewards/{customerId}` - Get rewards for a specific customer.

## Sample Data

Transactions are in-memory and include two customers over 3 months.
