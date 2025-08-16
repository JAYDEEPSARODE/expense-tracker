package com.expensetracker.expenseService.ExpenseService;

import com.expensetracker.expenseService.entity.ExpenseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpenseService {

    Object addExpense(ExpenseEntity request);

    List<ExpenseEntity> getExpenseDetails(String userEmail);
}
