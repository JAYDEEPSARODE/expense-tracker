package com.expensetracker.expenseService.ExpenseService;

import com.expensetracker.expenseService.entity.ExpenseEntity;
import com.expensetracker.expenseService.expenseRepo.ExpenseRepository;
import com.expensetracker.expenseService.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ExpenseServiceImpl implements ExpenseService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExpenseRepository expenseRepository;

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
            return expenseRepository.save(request);
        }catch (Exception e){
            log.error("Error While adding  Expense: " + e.getMessage());
            e.printStackTrace();
            throw new BadRequestException("Error While adding  Expense: " + e.getMessage());
        }
    }

    public void validateExpense(ExpenseEntity request) throws Exception {
        if (request.getCategory() == null || request.getAmount() == null) {
            throw new BadRequestException("Category cannot be empty");
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
