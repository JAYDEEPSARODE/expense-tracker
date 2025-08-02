package com.expensetracker.expenseService.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "USER_EXPENSES")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_expenses_seq")
    @SequenceGenerator(name = "user_expenses_seq", sequenceName = "USER_EXPENSES_SEQ", allocationSize = 1)
    @Column(name = "EXPENSE_ID")
    private Long expenseid;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "CREATED_BY", nullable = false)
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "CREATED_ON", nullable = false)
    private java.time.LocalDateTime createdOn;

    @Column(name = "UPDATED_ON")
    private java.time.LocalDateTime updatedOn;

    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "CATEGORY", nullable = false)
    private String category;

    @Column(name = "EXPENSE_DATE", nullable = false)
    private java.time.LocalDate expenseDate;

    @Column(name = "NOTE")
    private String note;

    @Transient
    private String userEmail; // This field is not persisted in the database, used for convenience in service layer
}