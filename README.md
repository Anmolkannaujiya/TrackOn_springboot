# Task Manager

A full-stack task management application built with **Spring Boot, React, MySQL, Spring Data JPA, Spring Security, and JWT authentication**.

The project provides secure user registration and login, user-specific task management, validation, pagination, filtering, and a basic React frontend for managing tasks.

## Features

- User registration with email and password
- BCrypt password hashing
- Email-based login
- JWT-based stateless authentication
- Spring Security integration
- User-specific task ownership
- Create, read, update, and delete tasks
- Prevent users from accessing or modifying other users' tasks
- Task status tracking
- Pagination and status filtering
- DTO and Mapper based API design
- Bean Validation
- Global exception handling
- Standard HTTP response codes
- Basic React frontend
- Login and registration UI
- Create, edit, delete, and view tasks
- Logout using JWT removal from browser storage
- CORS configuration for React and Spring Boot communication

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT
- BCrypt
- MySQL
- Maven

### Frontend
- React.js
- Vite
- JavaScript
- Fetch API

### Tools
- IntelliJ IDEA
- Postman
- Git
- GitHub
- MySQL

## Project Architecture

The backend follows a layered architecture:

```text
Client / React
      |
      v
Spring Security
      |
      v
Controller
      |
      v
Service
      |
      +----> DTO / Mapper
      |
      v
Repository
      |
      v
MySQL
```

Main backend packages:

```text
com.example.taskmanager
|
+-- controller
+-- dto
+-- entity
+-- enums
+-- exception
+-- mapper
+-- repository
+-- security
+-- service
```

## Authentication Flow

```text
User enters email + password
        |
        v
AuthController
        |
        v
AuthService
        |
        v
AuthenticationManager
        |
        v
CustomUserDetailsService
        |
        v
UserRepository
        |
        v
BCrypt password verification
        |
        v
JWT generated
        |
        v
JWT returned to frontend
```

For protected requests:

```text
Authorization: Bearer <JWT>
        |
        v
JwtAuthenticationFilter
        |
        v
JWT validation
        |
        v
Email extracted
        |
        v
UserDetails loaded
        |
        v
SecurityContext populated
        |
        v
Protected API endpoint
```

## Task Ownership and Authorization

Each task belongs to exactly one user.

The `Task` entity contains a many-to-one relationship with `User`:

```text
User 1 -------- * Task
```

The frontend does not send a `userId` while creating a task.

Instead, the backend identifies the logged-in user from the JWT and assigns ownership automatically.

For example:

```text
JWT
 |
 v
Authenticated email
 |
 v
Current User
 |
 v
Task.user = currentUser
```

Task retrieval, update, and deletion use both the task ID and current user ID. This prevents one authenticated user from accessing another user's tasks.

## Main API Endpoints

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
```

Example registration request:

```json
{
  "username": "Anmol",
  "email": "anmol@example.com",
  "password": "password123"
}
```

Example login request:

```json
{
  "email": "anmol@example.com",
  "password": "password123"
}
```

Example login response:

```json
{
  "token": "eyJhbGciOi...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

### Tasks

```http
POST   /api/tasks
GET    /api/tasks
GET    /api/tasks/{id}
PUT    /api/tasks/{id}
DELETE /api/tasks/{id}
```

Example task request:

```json
{
  "title": "Learn Spring Security",
  "description": "Complete JWT authentication",
  "status": "PENDING"
}
```

Supported task statuses:

```text
PENDING
IN_PROGRESS
COMPLETED
```

Pagination and filtering examples:

```http
GET /api/tasks?page=0&size=10
GET /api/tasks?status=PENDING
GET /api/tasks?page=0&size=10&sort=createdAt,desc
```

## Running the Backend

### Prerequisites

- Java 21
- Maven
- MySQL

Create a MySQL database:

```sql
CREATE DATABASE task_manager_db;
```

Configure the application in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_manager_db
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=${JWT_SECRET:task-manager-development-secret-key-2026-secure}
jwt.expiration=86400000
```

Run the backend:

```bash
mvn spring-boot:run
```

The API runs by default at:

```text
http://localhost:8080
```

## Running the Frontend

Go to the React project directory:

```bash
cd taskmanager-frontend
```

Install dependencies:

```bash
npm install
```

Run the development server:

```bash
npm run dev
```

The frontend normally runs at:

```text
http://localhost:5173
```

The Spring Boot backend allows this origin through its CORS configuration.

## Frontend Functionality

The current React frontend supports:

- Register
- Login
- JWT storage in `localStorage`
- Load authenticated user's tasks
- Create task
- Update task
- Delete task
- Logout
- Conditional UI based on authentication state

For protected API requests, React sends:

```http
Authorization: Bearer <JWT>
```

## Validation and Error Handling

Request DTOs use Jakarta Bean Validation annotations such as:

```text
@NotBlank
@Email
@Size
@NotNull
```

The backend also contains centralized exception handling using:

```text
@RestControllerAdvice
@ExceptionHandler
```

Custom task errors are returned using a standardized `ErrorResponse`.

## Security Notes

- Passwords are never stored as plain text.
- Passwords are hashed using BCrypt.
- JWT authentication is stateless.
- Email is the authentication identifier.
- `username` is only a display/name field and is not required to be unique.
- Task ownership is determined by the authenticated backend user, not by a user ID supplied from the frontend.
- JWT secrets can be provided using the `JWT_SECRET` environment variable.

## Current Project Status

Implemented:

- Secure backend CRUD
- User entity and authentication
- JWT authentication
- Spring Security
- User-task ownership
- User-specific task authorization
- Pagination and filtering
- Validation and global exception handling
- Basic React frontend

Planned improvements:

- Role-based ADMIN endpoints
- More specific authentication exceptions
- Improved frontend styling and routing
- Docker and Docker Compose
- GitHub Actions CI/CD
- AWS deployment
- Automated backend tests

## Future Deployment Architecture

```text
GitHub
   |
   v
GitHub Actions
   |
   v
Docker
   |
   v
AWS
```

## Author

**Anmol Kannaujiya**

B.Tech Computer Science & Engineering  
Lovely Professional University
