package com.expensetracker.expenseService.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long categoryId;
    private String name;
    private String description;
    private String icon;
    private String color;
}
