# Expense Tracker API - cURL Commands for Testing

This document contains all cURL commands to test the Expense Tracker microservices application.

**Base URL:** `http://localhost:8080` (API Gateway)

**Note:** Replace `YOUR_JWT_TOKEN` with the actual JWT token received from the login endpoint.

---

## 1. User Management

### 1.1 Register a New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123",
    "name": "John Doe"
  }'
```

### 1.2 Login and Get JWT Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

**Response Example:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Save the token for subsequent requests:**
```bash
export JWT_TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## 2. Category Management

**Note:** Categories are accessed directly through expense-service (port 8082) or you can add them through the expense-service endpoint.

### 2.1 Create a Category
```bash
curl -X POST http://localhost:8082/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "Food",
    "description": "Food and dining expenses",
    "icon": "🍔",
    "color": "#FF5733"
  }'
```

### 2.2 Create More Categories
```bash
# Transportation Category
curl -X POST http://localhost:8082/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "Transportation",
    "description": "Transportation expenses",
    "icon": "🚗",
    "color": "#3498DB"
  }'

# Entertainment Category
curl -X POST http://localhost:8082/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "Entertainment",
    "description": "Entertainment expenses",
    "icon": "🎬",
    "color": "#9B59B6"
  }'

# Utilities Category
curl -X POST http://localhost:8082/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "Utilities",
    "description": "Utility bills",
    "icon": "💡",
    "color": "#F39C12"
  }'
```

### 2.3 Get All Categories
```bash
curl -X GET http://localhost:8082/categories \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 2.4 Get Category by ID
```bash
curl -X GET http://localhost:8082/categories/1 \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 2.5 Get Category by Name
```bash
curl -X GET http://localhost:8082/categories/name/Food \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 2.6 Update Category
```bash
curl -X PUT http://localhost:8082/categories/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "Food & Dining",
    "description": "Updated description",
    "icon": "🍽️",
    "color": "#FF5733"
  }'
```

### 2.7 Delete Category
```bash
curl -X DELETE http://localhost:8082/categories/1 \
  -H "Authorization: Bearer $JWT_TOKEN"
```

---

## 3. Role Management

### 3.1 Create a Role
```bash
curl -X POST http://localhost:8082/roles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "ADMIN",
    "description": "Administrator role with full access"
  }'
```

### 3.2 Create More Roles
```bash
# User Role
curl -X POST http://localhost:8082/roles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "USER",
    "description": "Regular user role"
  }'

# Manager Role
curl -X POST http://localhost:8082/roles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "MANAGER",
    "description": "Manager role with extended permissions"
  }'
```

### 3.3 Get All Roles
```bash
curl -X GET http://localhost:8082/roles \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 3.4 Get Role by ID
```bash
curl -X GET http://localhost:8082/roles/1 \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 3.5 Get Role by Name
```bash
curl -X GET http://localhost:8082/roles/name/ADMIN \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 3.6 Update Role
```bash
curl -X PUT http://localhost:8082/roles/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "ADMIN",
    "description": "Updated administrator role description"
  }'
```

### 3.7 Delete Role
```bash
curl -X DELETE http://localhost:8082/roles/1 \
  -H "Authorization: Bearer $JWT_TOKEN"
```

---

## 4. Expense Management

### 4.1 Add an Expense
```bash
curl -X POST http://localhost:8080/api/expenses/addExpense \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "amount": 45.50,
    "category": "Food",
    "expenseDate": "2024-01-15",
    "note": "Lunch at restaurant"
  }'
```

**Note:** After adding an expense, the user will automatically receive an email notification with expense details and monthly total.

### 4.2 Add More Expenses
```bash
# Transportation Expense
curl -X POST http://localhost:8080/api/expenses/addExpense \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "amount": 25.00,
    "category": "Transportation",
    "expenseDate": "2024-01-16",
    "note": "Uber ride"
  }'

# Entertainment Expense
curl -X POST http://localhost:8080/api/expenses/addExpense \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "amount": 120.00,
    "category": "Entertainment",
    "expenseDate": "2024-01-17",
    "note": "Movie tickets"
  }'

# Utilities Expense
curl -X POST http://localhost:8080/api/expenses/addExpense \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "amount": 150.00,
    "category": "Utilities",
    "expenseDate": "2024-01-18",
    "note": "Electricity bill"
  }'

# Another Food Expense
curl -X POST http://localhost:8080/api/expenses/addExpense \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "amount": 30.00,
    "category": "Food",
    "expenseDate": "2024-01-19",
    "note": "Dinner"
  }'
```

### 4.3 Get All Expenses for a User
```bash
curl -X GET http://localhost:8080/api/expenses/getExpenses/john.doe@example.com \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 4.4 Get Expense by ID
```bash
curl -X GET http://localhost:8080/api/expenses/1 \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 4.5 Update an Expense
```bash
curl -X PUT http://localhost:8080/api/expenses/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "amount": 50.00,
    "category": "Food",
    "note": "Updated lunch expense"
  }'
```

### 4.6 Delete an Expense
```bash
curl -X DELETE http://localhost:8080/api/expenses/1 \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 4.7 Get Expenses by Category
```bash
curl -X GET http://localhost:8080/api/expenses/category/Food \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 4.8 Get Expenses by Date Range
```bash
curl -X GET "http://localhost:8080/api/expenses/date-range?startDate=2024-01-01&endDate=2024-01-31" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 4.9 Get Expenses by Category and Date Range
```bash
curl -X GET "http://localhost:8080/api/expenses/category/Food/date-range?startDate=2024-01-01&endDate=2024-01-31" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 4.10 Get Total Expenses for User
```bash
curl -X GET http://localhost:8080/api/expenses/total \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 4.11 Get Total Expenses by Date Range
```bash
curl -X GET "http://localhost:8080/api/expenses/total/date-range?startDate=2024-01-01&endDate=2024-01-31" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 4.12 Get Total Expenses by Category
```bash
curl -X GET http://localhost:8080/api/expenses/total/category/Food \
  -H "Authorization: Bearer $JWT_TOKEN"
```

---

## 5. Notification Service

### 5.1 Send a Notification
```bash
curl -X POST http://localhost:8080/api/notifications/send \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "userEmail": "john.doe@example.com",
    "type": "EMAIL",
    "subject": "Test Notification",
    "message": "This is a test notification message."
  }'
```

### 5.2 Get Notifications by User
```bash
curl -X GET http://localhost:8080/api/notifications/user/john.doe@example.com \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 5.3 Get Notifications by Status
```bash
curl -X GET http://localhost:8080/api/notifications/status/SENT \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 5.4 Get Notifications by User and Status
```bash
curl -X GET http://localhost:8080/api/notifications/user/john.doe@example.com/status/SENT \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 5.5 Get Notification by ID
```bash
curl -X GET http://localhost:8080/api/notifications/1 \
  -H "Authorization: Bearer $JWT_TOKEN"
```

---

## 6. Audit Service

### 6.1 Create Audit Log
```bash
curl -X POST http://localhost:8080/api/audit/log \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "userEmail": "john.doe@example.com",
    "action": "CREATE",
    "entityType": "EXPENSE",
    "entityId": "1",
    "requestMethod": "POST",
    "requestUrl": "/api/expenses/addExpense",
    "requestBody": "{\"amount\":45.50,\"category\":\"Food\"}",
    "responseStatus": 200,
    "ipAddress": "192.168.1.1"
  }'
```

### 6.2 Get Audit Logs by User
```bash
curl -X GET http://localhost:8080/api/audit/user/john.doe@example.com \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 6.3 Get Audit Logs by Entity
```bash
curl -X GET http://localhost:8080/api/audit/entity/EXPENSE/1 \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 6.4 Get Audit Logs by Date Range
```bash
curl -X GET "http://localhost:8080/api/audit/date-range?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 6.5 Get Audit Logs by User and Date Range
```bash
curl -X GET "http://localhost:8080/api/audit/user/john.doe@example.com/date-range?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

---

## 7. Complete Test Flow

Here's a complete test flow from user registration to expense creation:

```bash
# Step 1: Register a new user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "testpass123",
    "name": "Test User"
  }'

# Step 2: Login and get JWT token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "testpass123"
  }' | grep -o '"token":"[^"]*' | cut -d'"' -f4)

echo "JWT Token: $TOKEN"

# Step 3: Create a category
curl -X POST http://localhost:8082/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Food",
    "description": "Food expenses",
    "icon": "🍔",
    "color": "#FF5733"
  }'

# Step 4: Add an expense (this will trigger a notification)
curl -X POST http://localhost:8080/api/expenses/addExpense \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "amount": 75.50,
    "category": "Food",
    "expenseDate": "2024-01-20",
    "note": "Test expense - lunch"
  }'

# Step 5: Get all expenses
curl -X GET http://localhost:8080/api/expenses/getExpenses/testuser@example.com \
  -H "Authorization: Bearer $TOKEN"

# Step 6: Get total expenses
curl -X GET http://localhost:8080/api/expenses/total \
  -H "Authorization: Bearer $TOKEN"

# Step 7: Check notifications
curl -X GET http://localhost:8080/api/notifications/user/testuser@example.com \
  -H "Authorization: Bearer $TOKEN"
```

---

## 8. Testing Notes

### Important Points:
1. **JWT Token**: All protected endpoints require a valid JWT token in the `Authorization: Bearer <token>` header
2. **User Email**: The user email is extracted from the JWT token by the API Gateway and passed as `X-User-Email` header
3. **Notification**: When an expense is added, an automatic email notification is sent with expense details and monthly total
4. **Date Format**: Use ISO date format (YYYY-MM-DD) for dates
5. **Service Ports**:
   - API Gateway: 8080
   - User Service: 8081
   - Expense Service: 8082
   - Audit Service: 8083
   - Notification Service: 8084
   - Discovery Server: 8761

### Direct Service Access:
If you need to access services directly (bypassing API Gateway):
- User Service: `http://localhost:8081`
- Expense Service: `http://localhost:8082`
- Audit Service: `http://localhost:8083`
- Notification Service: `http://localhost:8084`

### Error Responses:
- **401 Unauthorized**: Invalid or missing JWT token
- **400 Bad Request**: Invalid request data
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

---

## 9. Quick Test Script

Save this as `test-api.sh` and make it executable:

```bash
#!/bin/bash

BASE_URL="http://localhost:8080"
EMAIL="testuser@example.com"
PASSWORD="testpass123"

echo "=== Testing Expense Tracker API ==="

# Register
echo -e "\n1. Registering user..."
curl -s -X POST $BASE_URL/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\",\"name\":\"Test User\"}" | jq .

# Login
echo -e "\n2. Logging in..."
TOKEN=$(curl -s -X POST $BASE_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}" | grep -o '"token":"[^"]*' | cut -d'"' -f4)

echo "Token: $TOKEN"

# Add Expense
echo -e "\n3. Adding expense..."
curl -s -X POST $BASE_URL/api/expenses/addExpense \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "amount": 100.00,
    "category": "Food",
    "expenseDate": "2024-01-20",
    "note": "Test expense"
  }' | jq .

# Get Expenses
echo -e "\n4. Getting expenses..."
curl -s -X GET $BASE_URL/api/expenses/getExpenses/$EMAIL \
  -H "Authorization: Bearer $TOKEN" | jq .

# Get Total
echo -e "\n5. Getting total expenses..."
curl -s -X GET $BASE_URL/api/expenses/total \
  -H "Authorization: Bearer $TOKEN" | jq .

echo -e "\n=== Test Complete ==="
```

Make it executable and run:
```bash
chmod +x test-api.sh
./test-api.sh
```

---

## 10. Windows PowerShell Commands

For Windows users, here are PowerShell equivalents:

### Register User
```powershell
$body = @{
    email = "john.doe@example.com"
    password = "password123"
    name = "John Doe"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method Post -Body $body -ContentType "application/json"
```

### Login
```powershell
$body = @{
    email = "john.doe@example.com"
    password = "password123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body $body -ContentType "application/json"
$token = $response.token
Write-Host "Token: $token"
```

### Add Expense
```powershell
$headers = @{
    Authorization = "Bearer $token"
    "Content-Type" = "application/json"
}

$body = @{
    amount = 45.50
    category = "Food"
    expenseDate = "2024-01-15"
    note = "Lunch at restaurant"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/expenses/addExpense" -Method Post -Headers $headers -Body $body
```

---

**Happy Testing! 🚀**
