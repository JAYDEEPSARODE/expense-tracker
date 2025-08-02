package com.expensetracker.expenseService.ExpenseService;

import com.expensetracker.expenseService.entity.ExpenseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ExpenseService {

    Object addExpense(ExpenseEntity request);
}
