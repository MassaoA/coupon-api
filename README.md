# Coupon API

REST API for coupon management, built with Java 21 and Spring Boot 4.

## Technologies

- Java 21
- Spring Boot 4
- Spring Data JPA
- H2 (in-memory database)
- Lombok
- SpringDoc OpenAPI (Swagger)
- Docker & Docker Compose
- JUnit 5 + Mockito

## Architecture

The project follows **Clean Architecture** principles, organized into four layers:

```
src/main/java/com/marcioaraki/coupon_api
├── domain          # Entities, use case interfaces, repository interfaces, exceptions
├── application     # Use case implementations (services)
├── infrastructure  # JPA entities, Spring Data repositories
└── api             # Controllers, DTOs, exception handler
```

All business rules are encapsulated in the `Coupon` domain entity.

## Business Rules

- Coupon code is alphanumeric with exactly 6 characters — special characters are removed automatically before saving
- Minimum discount value: 0.5
- Expiration date cannot be in the past
- A coupon can be created as already published
- Delete is a soft delete — the record is kept in the database with status `DELETED`
- A coupon that is already deleted cannot be deleted again

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/coupon` | Create a new coupon |
| GET | `/coupon/{id}` | Get coupon by ID |
| DELETE | `/coupon/{id}` | Soft delete a coupon |

## Running with Docker

```bash
docker-compose up --build
```

## Running locally

```bash
./gradlew bootRun
```

## Running tests

```bash
./gradlew test
```

## API Documentation

After starting the application, access:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **H2 Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:coupondb`
  - User: `sa`
  - Password: *(empty)*
