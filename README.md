# Employee Management Service

This is a Spring Boot application that provides RESTful APIs for managing employees, including creating, updating, deleting, and retrieving employee records with filtering and pagination options.

## Features

- Create a new employee record
- Update an existing employee record
- Delete an employee by ID
- Retrieve all employees with pagination and filtering options (by age and title)
- Exception handling with custom exceptions
- JSON responses with custom formatting

## Technologies Used

- **Java 11** or higher
- **Spring Boot** 3.x
- **Spring Data JPA**
- **H2 Database** (in-memory database)
- **JUnit 5** for unit testing
- **Mockito** for mocking in unit tests
- **Jackson** for JSON processing

## Prerequisites

To run this project, you need to have:

- Java Development Kit (JDK) 11 or higher
- Maven 3.x or higher

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/eswarpalani/employee-service.git
cd employee-service
mvn spring-boot:run
##API document
http://localhost:8080/swagger-ui/index.html
