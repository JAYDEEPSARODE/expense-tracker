# Quick Test Guide - Expense Tracker API

## Prerequisites
- All services should be running
- API Gateway: http://localhost:8080
- Expense Service (direct): http://localhost:8082

## Quick Start

### 1. Register & Login
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"pass123","name":"Test User"}'

# Login (save the token)
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"pass123"}' | grep -o '"token":"[^"]*' | cut -d'"' -f4)
```

### 2. Create Category (Direct to Expense Service)
```bash
curl -X POST http://localhost:8082/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Food","description":"Food expenses","icon":"🍔","color":"#FF5733"}'
```

### 3. Add Expense (Triggers Notification)
```bash
curl -X POST http://localhost:8080/api/expenses/addExpense \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "amount": 50.00,
    "category": "Food",
    "expenseDate": "2024-01-20",
    "note": "Lunch"
  }'
```

### 4. Get Expenses
```bash
curl -X GET http://localhost:8080/api/expenses/getExpenses/user@test.com \
  -H "Authorization: Bearer $TOKEN"
```

### 5. Get Total Expenses
```bash
curl -X GET http://localhost:8080/api/expenses/total \
  -H "Authorization: Bearer $TOKEN"
```

## Complete Flow Test

```bash
#!/bin/bash
EMAIL="test@example.com"
PASS="password123"
BASE="http://localhost:8080"
EXPENSE_SERVICE="http://localhost:8082"

echo "1. Registering..."
curl -s -X POST $BASE/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASS\",\"name\":\"Test\"}"

echo -e "\n\n2. Logging in..."
TOKEN=$(curl -s -X POST $BASE/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASS\"}" | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Token: $TOKEN"

echo -e "\n3. Creating category..."
curl -s -X POST $EXPENSE_SERVICE/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Food","description":"Food","icon":"🍔","color":"#FF5733"}'

echo -e "\n\n4. Adding expense (will send notification)..."
curl -s -X POST $BASE/api/expenses/addExpense \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"amount\":100,\"category\":\"Food\",\"expenseDate\":\"2024-01-20\",\"note\":\"Test\"}"

echo -e "\n\n5. Getting expenses..."
curl -s -X GET $BASE/api/expenses/getExpenses/$EMAIL \
  -H "Authorization: Bearer $TOKEN" | jq .

echo -e "\n\n6. Getting total..."
curl -s -X GET $BASE/api/expenses/total \
  -H "Authorization: Bearer $TOKEN" | jq .

echo -e "\n\nDone!"
```
