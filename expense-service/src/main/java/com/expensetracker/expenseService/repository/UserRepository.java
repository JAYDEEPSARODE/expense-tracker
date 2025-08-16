package com.expensetracker.expenseService.repository;

import com.expensetracker.expenseService.entity.ExpenseEntity;
import com.expensetracker.expenseService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    String getIdByEmail(@Param("email") String email);

    String GET_EXPENSE_DETAILS = "SELECT e FROM ExpenseEntity e WHERE e.userId = (SELECT u.id FROM User u WHERE u.email = :email)";
    @Query(GET_EXPENSE_DETAILS)
    List<ExpenseEntity> getExpenseDetails(@Param("email") String email);
}
