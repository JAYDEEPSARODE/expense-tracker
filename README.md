# Expense Tracker Microservices Application

A comprehensive microservices-based expense tracking application built with Spring Boot, featuring user management, expense tracking, notifications, audit logging, and more.

## Architecture

This application follows a microservices architecture with the following services:

1. **Discovery Server (Eureka)** - Service discovery and registration
2. **API Gateway** - Single entry point with JWT authentication
3. **User Service** - User registration, authentication, and JWT token generation
4. **Expense Service** - Core expense management with CRUD operations, filtering, and analytics
5. **Audit Service** - Comprehensive audit logging for all operations
6. **Notification Service** - Email, SMS, and push notification support

## Technology Stack

- **Java 17**
- **Spring Boot 3.3.13**
- **Spring Cloud 2023.0.6**
- **Spring Security** - Authentication and authorization
- **JWT (JSON Web Tokens)** - Stateless authentication
- **Spring Data JPA** - Database operations
- **Oracle Database** - Primary database
- **Redis** - Caching layer
- **Eureka** - Service discovery
- **Spring Cloud Gateway** - API Gateway
- **Feign Client** - Declarative HTTP client for service-to-service communication
- **Maven** - Build tool
- **Lombok** - Boilerplate code reduction

## Services Overview

### 1. Discovery Server (Port: 8761)
- Eureka server for service registration and discovery
- All microservices register with this server

### 2. API Gateway (Port: 8080)
- Routes requests to appropriate microservices
- JWT authentication filter
- Public endpoints: `/api/auth/login`, `/api/auth/register`
- Protected endpoints require JWT token in Authorization header

### 3. User Service (Port: 8081)
**Endpoints:**
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login and get JWT token
- `GET /getUserDetails` - Get user details (protected)

**Features:**
- User registration with password encryption
- JWT token generation
- Spring Security integration

### 4. Expense Service (Port: 8082)
**Endpoints:**
- `POST /expenses/addExpense` - Add new expense
- `GET /expenses/getExpenses/{userEmail}` - Get all expenses for user
- `GET /expenses/{expenseId}` - Get expense by ID
- `PUT /expenses/{expenseId}` - Update expense
- `DELETE /expenses/{expenseId}` - Delete expense
- `GET /expenses/category/{category}` - Get expenses by category
- `GET /expenses/date-range?startDate=&endDate=` - Get expenses by date range
- `GET /expenses/category/{category}/date-range?startDate=&endDate=` - Get expenses by category and date range
- `GET /expenses/total` - Get total expenses for user
- `GET /expenses/total/date-range?startDate=&endDate=` - Get total expenses in date range
- `GET /expenses/total/category/{category}` - Get total expenses by category

**Category Management:**
- `POST /categories` - Create category
- `GET /categories` - Get all categories
- `GET /categories/{categoryId}` - Get category by ID
- `PUT /categories/{categoryId}` - Update category
- `DELETE /categories/{categoryId}` - Delete category
- `GET /categories/name/{name}` - Get category by name

**Role Management:**
- `POST /roles` - Create role
- `GET /roles` - Get all roles
- `GET /roles/{roleId}` - Get role by ID
- `PUT /roles/{roleId}` - Update role
- `DELETE /roles/{roleId}` - Delete role
- `GET /roles/name/{name}` - Get role by name

**Features:**
- Full CRUD operations for expenses
- Category and role management
- Expense filtering by category, date range, or both
- Total expense calculations
- Redis caching for performance
- User-specific expense access control
- Feign Client integration for service-to-service communication with notification service
- Automatic email notifications on expense creation

### 5. Audit Service (Port: 8083)
**Endpoints:**
- `POST /audit/log` - Create audit log
- `GET /audit/user/{userEmail}` - Get audit logs by user
- `GET /audit/entity/{entityType}/{entityId}` - Get audit logs by entity
- `GET /audit/date-range?startDate=&endDate=` - Get audit logs by date range
- `GET /audit/user/{userEmail}/date-range?startDate=&endDate=` - Get audit logs by user and date range

**Features:**
- Comprehensive audit logging
- Track all user actions
- Entity-based audit trails
- Date range filtering

### 6. Notification Service (Port: 8084)
**Endpoints:**
- `POST /notifications/send` - Send notification (EMAIL, SMS, or PUSH)
- `GET /notifications/user/{userEmail}` - Get notifications by user
- `GET /notifications/status/{status}` - Get notifications by status
- `GET /notifications/user/{userEmail}/status/{status}` - Get notifications by user and status
- `GET /notifications/{notificationId}` - Get notification by ID

**Features:**
- Email notifications (SMTP)
- SMS notifications (placeholder for integration)
- Push notifications (placeholder for integration)
- Notification status tracking
- Error handling and logging

## Database Schema

### Users Table
- id (Primary Key)
- email (Unique)
- password (Encrypted)
- name

### Expenses Table (USER_EXPENSES)
- expense_id (Primary Key)
- user_id (Foreign Key)
- amount
- category
- expense_date
- note
- created_by
- created_on
- updated_by
- updated_on

### Categories Table
- category_id (Primary Key)
- name (Unique)
- description
- icon
- color

### Roles Table
- role_id (Primary Key)
- name (Unique)
- description

### Audit Logs Table
- audit_id (Primary Key)
- user_email
- action
- entity_type
- entity_id
- request_method
- request_url
- request_body
- response_status
- ip_address
- timestamp

### Notifications Table
- notification_id (Primary Key)
- user_email
- type (EMAIL, SMS, PUSH)
- subject
- message
- status (PENDING, SENT, FAILED)
- created_at
- sent_at
- error_message

## Setup Instructions

### Prerequisites
1. Java 17 or higher
2. Maven 3.6+
3. Oracle Database (or update to MySQL/PostgreSQL)
4. Redis Server
5. SMTP server credentials (for email notifications)

### Configuration

1. **Database Configuration**
   Update `application.properties` in each service with your database credentials:
   ```properties
   spring.datasource.url=jdbc:oracle:thin:@localhost:1522:xe
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```

2. **Redis Configuration**
   Ensure Redis is running on `localhost:6379` or update the configuration in expense-service.

3. **Email Configuration**
   Update notification-service `application.properties`:
   ```properties
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   ```

4. **JWT Secret**
   Ensure all services use the same JWT secret key (configured in application.properties).

5. **Feign Client Configuration**
   Feign Client is used for service-to-service communication. It automatically integrates with Eureka for service discovery. The configuration is in `application.properties`:
   ```properties
   feign.client.config.default.connectTimeout=5000
   feign.client.config.default.readTimeout=10000
   ```

### Service Communication Architecture

The application uses **Feign Client** for inter-service communication:

- **Expense Service → Notification Service**: Uses Feign Client to send notifications
- **Service Discovery**: Feign automatically discovers services through Eureka
- **Fallback Mechanism**: Includes fallback handlers for graceful degradation when services are unavailable
- **Load Balancing**: Feign integrates with Spring Cloud LoadBalancer for distributed service calls

**Benefits of Feign Client:**
- Declarative REST client (interface-based)
- Automatic service discovery via Eureka
- Built-in load balancing
- Fallback support for resilience
- Cleaner code compared to RestTemplate

### Running the Application

1. **Start Discovery Server**
   ```bash
   cd discovery-server
   mvn spring-boot:run
   ```

2. **Start API Gateway**
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

3. **Start User Service**
   ```bash
   cd user-service
   mvn spring-boot:run
   ```

4. **Start Expense Service**
   ```bash
   cd expense-service
   mvn spring-boot:run
   ```

5. **Start Audit Service**
   ```bash
   cd audit-service
   mvn spring-boot:run
   ```

6. **Start Notification Service**
   ```bash
   cd notification-service
   mvn spring-boot:run
   ```

## API Usage Examples

### 1. Register User
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe"
}
```

### 2. Login
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. Add Expense
```bash
POST http://localhost:8080/api/expenses/addExpense
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "amount": 100.50,
  "category": "Food",
  "expenseDate": "2024-01-15",
  "note": "Lunch at restaurant"
}
```

### 4. Get Expenses
```bash
GET http://localhost:8080/api/expenses/getExpenses/user@example.com
Authorization: Bearer <JWT_TOKEN>
```

### 5. Get Expenses by Category
```bash
GET http://localhost:8080/api/expenses/category/Food
Authorization: Bearer <JWT_TOKEN>
```

### 6. Get Total Expenses
```bash
GET http://localhost:8080/api/expenses/total
Authorization: Bearer <JWT_TOKEN>
```

### 7. Send Notification
```bash
POST http://localhost:8080/api/notifications/send
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "userEmail": "user@example.com",
  "type": "EMAIL",
  "subject": "Expense Added",
  "message": "Your expense of $100.50 has been added successfully."
}
```

## Features

### ✅ Completed Features

1. **User Management**
   - User registration with password encryption
   - JWT-based authentication
   - User profile management

2. **Expense Management**
   - Add, update, delete expenses
   - View expenses with filtering
   - Category-based organization
   - Date range filtering
   - Total expense calculations

3. **Category Management**
   - CRUD operations for categories
   - Category-based expense filtering

4. **Role Management**
   - CRUD operations for roles
   - Role-based access control foundation

5. **Audit Logging**
   - Comprehensive audit trail
   - User action tracking
   - Entity-based logging

6. **Notifications**
   - Email notifications
   - SMS and push notification infrastructure
   - Notification status tracking

7. **Caching**
   - Redis integration for expense data
   - Cache invalidation on updates

8. **Security**
   - JWT authentication
   - Password encryption
   - API Gateway security filter

9. **Service Discovery**
   - Eureka integration
   - Dynamic service registration

## Project Structure

```
expense-tracker/
├── api-gateway/          # API Gateway service
├── audit-service/        # Audit logging service
├── common-lib/           # Shared library
├── discovery-server/     # Eureka discovery server
├── expense-service/      # Expense management service
├── notification-service/ # Notification service
└── user-service/         # User management service
```

## Future Enhancements

1. **Frontend Application** - React/Vue.js web application
2. **Mobile App** - React Native or Flutter mobile app
3. **Advanced Analytics** - Expense trends, charts, and reports
4. **Budget Management** - Set budgets and track spending
5. **Recurring Expenses** - Automate recurring expense tracking
6. **Export Functionality** - Export expenses to CSV/PDF
7. **Multi-currency Support** - Handle multiple currencies
8. **Receipt Upload** - Image upload and OCR for receipts
9. **Expense Sharing** - Share expenses with family/teams
10. **Integration** - Bank account integration, payment gateways

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please create an issue in the repository.
