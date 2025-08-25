# Ticketing Project - Backend

A comprehensive ticketing system backend built with Spring Boot, JPA, and MySQL.

## Features

- **User Management**: Create, read, update, and delete users with role-based access control
- **Ticket Management**: Full CRUD operations for tickets with status and priority management
- **Comment System**: Add comments to tickets with author tracking
- **Role-Based Access Control**: Admin, Manager, and User roles
- **RESTful API**: Clean REST endpoints with proper HTTP status codes
- **Data Validation**: Input validation using Bean Validation
- **Exception Handling**: Global exception handling with meaningful error messages
- **Pagination**: Support for paginated results
- **Search Functionality**: Search tickets by various criteria

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **Spring Security**
- **MySQL Database**
- **Maven**
- **Lombok**

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Setup Instructions

### 1. Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE ticketing_db;
```

2. Update `src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. Build and Run

1. Clone the repository:
```bash
git clone <repository-url>
cd TicketingProject
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 3. Default Admin User

The system automatically creates a default admin user:
- **Email**: admin@ticketing.com
- **Password**: admin123

## API Endpoints

### Users

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create a new user |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/email/{email}` | Get user by email |
| GET | `/api/users` | Get all users |
| GET | `/api/users/active` | Get active users only |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| PATCH | `/api/users/{id}/deactivate` | Deactivate user |

### Tickets

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/tickets?ownerId={id}` | Create a new ticket |
| GET | `/api/tickets/{id}` | Get ticket by ID |
| GET | `/api/tickets/code/{code}` | Get ticket by code |
| GET | `/api/tickets` | Get all tickets (paginated) |
| GET | `/api/tickets/owner/{ownerId}` | Get tickets by owner |
| GET | `/api/tickets/assignee/{assigneeId}` | Get tickets by assignee |
| GET | `/api/tickets/status/{status}` | Get tickets by status |
| GET | `/api/tickets/priority/{priority}` | Get tickets by priority |
| GET | `/api/tickets/search?searchTerm={term}` | Search tickets |
| PUT | `/api/tickets/{id}` | Update ticket |
| DELETE | `/api/tickets/{id}` | Delete ticket |
| PATCH | `/api/tickets/{id}/assign/{assigneeId}` | Assign ticket |
| PATCH | `/api/tickets/{id}/status/{status}` | Change ticket status |

### Comments

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/comments?authorId={id}` | Create a new comment |
| GET | `/api/comments/{id}` | Get comment by ID |
| GET | `/api/comments/ticket/{ticketId}` | Get comments by ticket |
| GET | `/api/comments/author/{authorId}` | Get comments by author |
| PUT | `/api/comments/{id}?body={text}&authorId={id}` | Update comment |
| DELETE | `/api/comments/{id}?authorId={id}` | Delete comment |
| GET | `/api/comments/ticket/{ticketId}/count` | Get comment count |

## Data Models

### User
- `id`: Primary key
- `fullName`: User's full name
- `email`: Unique email address
- `passwordHash`: Encrypted password
- `active`: Account status
- `roles`: Set of assigned roles
- `createdAt`: Creation timestamp
- `updatedAt`: Last update timestamp

### Ticket
- `id`: Primary key
- `code`: Unique ticket code (auto-generated)
- `subject`: Ticket subject
- `description`: Detailed description
- `status`: Current status (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
- `priority`: Priority level (LOW, MEDIUM, HIGH, CRITICAL)
- `owner`: User who created the ticket
- `assignee`: User assigned to work on the ticket
- `closedAt`: When the ticket was closed
- `comments`: List of comments
- `history`: Ticket change history

### Comment
- `id`: Primary key
- `ticket`: Associated ticket
- `author`: User who wrote the comment
- `body`: Comment content
- `createdAt`: Creation timestamp
- `updatedAt`: Last update timestamp

### Role
- `id`: Primary key
- `name`: Role name (ADMIN, MANAGER, USER)

## Query Parameters

### Pagination
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)
- `sortBy`: Sort field (default: createdAt)
- `sortDir`: Sort direction (asc/desc, default: desc)

### Example
```
GET /api/tickets?page=0&size=20&sortBy=priority&sortDir=asc
```

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- `200 OK`: Successful operation
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid input or business rule violation
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Unexpected server error

## Security

- Password encryption using BCrypt
- Role-based access control
- Input validation and sanitization
- CORS enabled for cross-origin requests

## Development

### Project Structure
```
src/main/java/org/example/ticketingproject/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities
├── exception/      # Exception handling
├── repository/     # Data access layer
└── service/        # Business logic layer
```

### Adding New Features

1. Create entity classes in the `entity` package
2. Create DTOs in the `dto` package
3. Create repository interfaces in the `repository` package
4. Implement business logic in the `service` package
5. Create REST endpoints in the `controller` package

## Testing

Run tests with:
```bash
mvn test
```

## Deployment

1. Build the JAR file:
```bash
mvn clean package
```

2. Run the JAR:
```bash
java -jar target/TicketingProject-0.0.1-SNAPSHOT.jar
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.
