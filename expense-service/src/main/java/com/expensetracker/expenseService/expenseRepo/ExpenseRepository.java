package com.expensetracker.expenseService.expenseRepo;

import com.expensetracker.expenseService.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
    
    List<ExpenseEntity> findByUserIdAndCategory(Long userId, String category);
    
    List<ExpenseEntity> findByUserIdAndExpenseDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    
    List<ExpenseEntity> findByUserIdAndCategoryAndExpenseDateBetween(Long userId, String category, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.userId = :userId")
    Double sumAmountByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.userId = :userId AND e.expenseDate BETWEEN :startDate AND :endDate")
    Double sumAmountByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.userId = :userId AND e.category = :category")
    Double sumAmountByUserIdAndCategory(@Param("userId") Long userId, @Param("category") String category);
}
