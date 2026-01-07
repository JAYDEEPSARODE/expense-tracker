package com.expensetracker.expenseService.ExpenseService;

import com.expensetracker.expenseService.entity.ExpenseEntity;
import com.expensetracker.expenseService.expenseRepo.ExpenseRepository;
import com.expensetracker.expenseService.repository.UserRepository;
import com.expensetracker.expenseService.service.NotificationClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ExpenseServiceImpl implements ExpenseService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    NotificationClient notificationClient;

    @Override
    @SneakyThrows
    public ExpenseEntity addExpense(ExpenseEntity request) {
        try{
            validateExpense(request);
            if(request.getExpenseDate() == null){
                request.setExpenseDate(LocalDate.now());
            }

            String userId = userRepository.getIdByEmail(request.getUserEmail());
            request.setCreatedBy(userId);
            request.setUserId(Long.valueOf(userId));
            request.setCreatedOn(LocalDateTime.now());
            
            ExpenseEntity savedExpense = expenseRepository.save(request);
            
            // Send notification after successful expense creation
            sendExpenseAddedNotification(savedExpense);
            
            return savedExpense;
        }catch (Exception e){
            log.error("Error While adding  Expense: " + e.getMessage());
            e.printStackTrace();
            throw new BadRequestException("Error While adding  Expense: " + e.getMessage());
        }
    }

    private void sendExpenseAddedNotification(ExpenseEntity expense) {
        try {
            // Get user email from the expense entity or fetch from repository
            String userEmail = expense.getUserEmail();
            if (userEmail == null || userEmail.isEmpty()) {
                // If userEmail is not in transient field, get it from user repository
                userEmail = userRepository.getEmailById(expense.getUserId());
                if (userEmail == null || userEmail.isEmpty()) {
                    log.warn("Could not find user email for expense notification");
                    return;
                }
            }
            
            LocalDate expenseDate = expense.getExpenseDate();
            
            // Calculate monthly total expenses
            YearMonth yearMonth = YearMonth.from(expenseDate);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();
            
            Double monthlyTotal = getTotalExpensesByUserAndDateRange(userEmail, startDate, endDate);
            
            // Build notification message
            String subject = "Expense Added Successfully";
            String message = buildExpenseNotificationMessage(expense, monthlyTotal, expenseDate);
            
            // Send notification asynchronously (don't block expense creation)
            notificationClient.sendNotification(userEmail, subject, message);
        } catch (Exception e) {
            log.error("Error sending expense notification: {}", e.getMessage());
            // Don't throw exception - notification failure shouldn't break expense creation
        }
    }

    private String buildExpenseNotificationMessage(ExpenseEntity expense, Double monthlyTotal, LocalDate expenseDate) {
        StringBuilder message = new StringBuilder();
        message.append("Your expense has been successfully added!\n\n");
        message.append("Expense Details:\n");
        message.append("• Amount: $").append(String.format("%.2f", expense.getAmount())).append("\n");
        message.append("• Category: ").append(expense.getCategory()).append("\n");
        message.append("• Date: ").append(expenseDate.toString()).append("\n");
        
        if (expense.getNote() != null && !expense.getNote().isEmpty()) {
            message.append("• Note: ").append(expense.getNote()).append("\n");
        }
        
        message.append("\n");
        message.append("Total Expenses for ").append(expenseDate.getMonth().toString())
                .append(" ").append(expenseDate.getYear()).append(": $")
                .append(String.format("%.2f", monthlyTotal));
        
        return message.toString();
    }

    @Override
    @SneakyThrows
    public ExpenseEntity getExpenseById(Long expenseId, String userEmail) {
        try {
            String userId = userRepository.getIdByEmail(userEmail);
            Optional<ExpenseEntity> expense = expenseRepository.findById(expenseId);
            
            if (expense.isEmpty()) {
                throw new BadRequestException("Expense not found");
            }
            
            if (!expense.get().getUserId().equals(Long.valueOf(userId))) {
                throw new BadRequestException("Unauthorized access to expense");
            }
            
            return expense.get();
        } catch (Exception e) {
            log.error("Error getting expense: " + e.getMessage());
            throw new BadRequestException("Error getting expense: " + e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public ExpenseEntity updateExpense(Long expenseId, ExpenseEntity request, String userEmail) {
        try {
            ExpenseEntity existingExpense = getExpenseById(expenseId, userEmail);
            
            if (request.getAmount() != null) {
                if (request.getAmount() <= 0) {
                    throw new BadRequestException("Amount must be greater than zero");
                }
                existingExpense.setAmount(request.getAmount());
            }
            
            if (request.getCategory() != null && !request.getCategory().isEmpty()) {
                existingExpense.setCategory(request.getCategory());
            }
            
            if (request.getExpenseDate() != null) {
                existingExpense.setExpenseDate(request.getExpenseDate());
            }
            
            if (request.getNote() != null) {
                existingExpense.setNote(request.getNote());
            }
            
            String userId = userRepository.getIdByEmail(userEmail);
            existingExpense.setUpdatedBy(userId);
            existingExpense.setUpdatedOn(LocalDateTime.now());
            
            return expenseRepository.save(existingExpense);
        } catch (Exception e) {
            log.error("Error updating expense: " + e.getMessage());
            throw new BadRequestException("Error updating expense: " + e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public void deleteExpense(Long expenseId, String userEmail) {
        try {
            ExpenseEntity expense = getExpenseById(expenseId, userEmail);
            expenseRepository.delete(expense);
        } catch (Exception e) {
            log.error("Error deleting expense: " + e.getMessage());
            throw new BadRequestException("Error deleting expense: " + e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public List<ExpenseEntity> getExpensesByCategory(String userEmail, String category) {
        try {
            String userId = userRepository.getIdByEmail(userEmail);
            return expenseRepository.findByUserIdAndCategory(Long.valueOf(userId), category);
        } catch (Exception e) {
            log.error("Error getting expenses by category: " + e.getMessage());
            throw new BadRequestException("Error getting expenses by category: " + e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public List<ExpenseEntity> getExpensesByDateRange(String userEmail, LocalDate startDate, LocalDate endDate) {
        try {
            String userId = userRepository.getIdByEmail(userEmail);
            return expenseRepository.findByUserIdAndExpenseDateBetween(Long.valueOf(userId), startDate, endDate);
        } catch (Exception e) {
            log.error("Error getting expenses by date range: " + e.getMessage());
            throw new BadRequestException("Error getting expenses by date range: " + e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public List<ExpenseEntity> getExpensesByCategoryAndDateRange(String userEmail, String category, LocalDate startDate, LocalDate endDate) {
        try {
            String userId = userRepository.getIdByEmail(userEmail);
            return expenseRepository.findByUserIdAndCategoryAndExpenseDateBetween(Long.valueOf(userId), category, startDate, endDate);
        } catch (Exception e) {
            log.error("Error getting expenses by category and date range: " + e.getMessage());
            throw new BadRequestException("Error getting expenses by category and date range: " + e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public Double getTotalExpensesByUser(String userEmail) {
        try {
            String userId = userRepository.getIdByEmail(userEmail);
            Double total = expenseRepository.sumAmountByUserId(Long.valueOf(userId));
            return total != null ? total : 0.0;
        } catch (Exception e) {
            log.error("Error calculating total expenses: " + e.getMessage());
            throw new BadRequestException("Error calculating total expenses: " + e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public Double getTotalExpensesByUserAndDateRange(String userEmail, LocalDate startDate, LocalDate endDate) {
        try {
            String userId = userRepository.getIdByEmail(userEmail);
            Double total = expenseRepository.sumAmountByUserIdAndDateRange(Long.valueOf(userId), startDate, endDate);
            return total != null ? total : 0.0;
        } catch (Exception e) {
            log.error("Error calculating total expenses by date range: " + e.getMessage());
            throw new BadRequestException("Error calculating total expenses by date range: " + e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public Double getTotalExpensesByCategory(String userEmail, String category) {
        try {
            String userId = userRepository.getIdByEmail(userEmail);
            Double total = expenseRepository.sumAmountByUserIdAndCategory(Long.valueOf(userId), category);
            return total != null ? total : 0.0;
        } catch (Exception e) {
            log.error("Error calculating total expenses by category: " + e.getMessage());
            throw new BadRequestException("Error calculating total expenses by category: " + e.getMessage());
        }
    }

    public void validateExpense(ExpenseEntity request) throws Exception {
        if (request.getCategory() == null || request.getAmount() == null) {
            throw new BadRequestException("Category and amount cannot be empty");
        }

        if (request.getAmount() <= 0) {
            throw new BadRequestException("Amount must be greater than zero");
        }
    }

    @SneakyThrows
    public List<ExpenseEntity> getExpenseDetails(String userEmailId){
        try{
            return userRepository.getExpenseDetails(userEmailId);
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }
}
