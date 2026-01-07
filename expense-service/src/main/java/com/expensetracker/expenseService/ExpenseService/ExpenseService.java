package com.expensetracker.expenseService.ExpenseService;

import com.expensetracker.expenseService.entity.ExpenseEntity;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseEntity addExpense(ExpenseEntity request);

    List<ExpenseEntity> getExpenseDetails(String userEmail);

    ExpenseEntity getExpenseById(Long expenseId, String userEmail);

    ExpenseEntity updateExpense(Long expenseId, ExpenseEntity request, String userEmail);

    void deleteExpense(Long expenseId, String userEmail);

    List<ExpenseEntity> getExpensesByCategory(String userEmail, String category);

    List<ExpenseEntity> getExpensesByDateRange(String userEmail, LocalDate startDate, LocalDate endDate);

    List<ExpenseEntity> getExpensesByCategoryAndDateRange(String userEmail, String category, LocalDate startDate, LocalDate endDate);

    Double getTotalExpensesByUser(String userEmail);

    Double getTotalExpensesByUserAndDateRange(String userEmail, LocalDate startDate, LocalDate endDate);

    Double getTotalExpensesByCategory(String userEmail, String category);
}
