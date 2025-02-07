# SchoolApp

## Introduction

SchoolApp is a Spring Boot application designed to manage school-related features such as contact messages, holiday listings, and more. It includes user authentication and authorization via Spring Security, enabling role-based access control.

## Features

- **Contact Messages**: Submit, view, and manage contact messages with status updates.
- **Holiday Management**: View a list of predefined holidays.
- **Role-Based Security**: Users are assigned roles (`USER` or `ADMIN`) with specific permissions.
- **Audit Logging**: Track method execution time and log exceptions using Aspect-Oriented Programming (AOP).
- **Pre-Configured In-Memory Authentication**: Simplifies user access with pre-defined credentials.

## Technologies Used

- **Backend**: Spring Boot, Spring Security, Spring MVC
- **Frontend**: Thymeleaf
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **AOP**: Spring AOP for logging
- **Password Hashing**: BCrypt

## Database Configuration

### Schema (`schema.sql`):

- **Database Name**: `springschool`
- **Tables**:
    - `contact_msg`: Stores contact message details.
    - `holidays`: Stores holiday details.
    - `person`: Stores user details.
    - `roles`: Stores user roles.
    - `address`: Stores user addresses.

### Sample Data (`data.sql`):

Pre-loaded holiday data, such as:

- New Year's Day
- Halloween
- Thanksgiving Day

To set up the database:

1. Create the database using the `schema.sql` file.
2. Load initial data using the `data.sql` file.

## Security

### Roles:

1. **USER**: Basic access to the dashboard.
2. **ADMIN**: Access to manage messages and restricted areas.

Passwords are hashed using BCrypt for secure storage.

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.0+

### Steps to Run Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/timebetov/SchoolApp.git
   ```
2. Navigate to the project directory:
   ```bash
   cd SchoolApp
   ```
3. Create the database using the `schema.sql` file.
4. Load the initial data using the `data.sql` file.
5. Update `application.properties` with your MySQL credentials if needed:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/springschool
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
6. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```
7. Access the application at [http://localhost:8080](http://localhost:8080).

## Endpoints

### Public Endpoints:

- `/home` - Home page
- `/about` - About page
- `/contact` - Contact form submission
- `/courses` - Courses page
- `/holidays` - List of holidays

### Secured Endpoints:

- `/dashboard` - Dashboard (Role: USER/ADMIN)
- `/displayMessages` - View all contact messages (Role: ADMIN)
- `/closeMsg` - Update message status to "Closed" (Role: ADMIN)

### Authentication Endpoints:

- `/login` - Login page
- `/logout` - Logout functionality

## Logging and AOP

- **Execution Time Logging**: Logs method execution times for performance monitoring.
- **Exception Logging**: Logs method exceptions for debugging purposes.

### Example Logs:

- `INFO: public boolean saveMessageDetails(Contact) method execution start`
- `ERROR: public boolean updateMsgStatus(int, String) method execution failed due to: <error_message>`

## Future Enhancements

- Implement pagination for contact message and holiday lists.
- Integrate RESTful APIs for external consumption.