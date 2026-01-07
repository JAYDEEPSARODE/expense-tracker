package com.expensetracker.expenseService.controller;

import com.expensetracker.expenseService.dto.CategoryDTO;
import com.expensetracker.expenseService.global.ResponseDTO;
import com.expensetracker.expenseService.service.CategoryService;
import com.expensetracker.expenseService.Utility.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createCategory(HttpServletRequest request, @RequestBody CategoryDTO categoryDTO) {
        String endPoint = "/categories";
        CategoryDTO created = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(created, endPoint), HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO> updateCategory(HttpServletRequest request, 
                                                       @PathVariable Long categoryId,
                                                       @RequestBody CategoryDTO categoryDTO) {
        String endPoint = "/categories/" + categoryId;
        CategoryDTO updated = categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(updated, endPoint), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO> deleteCategory(HttpServletRequest request, @PathVariable Long categoryId) {
        String endPoint = "/categories/" + categoryId;
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("Category deleted successfully", endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO> getCategoryById(HttpServletRequest request, @PathVariable Long categoryId) {
        String endPoint = "/categories/" + categoryId;
        CategoryDTO category = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(category, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAllCategories(HttpServletRequest request) {
        String endPoint = "/categories";
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(categories, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseDTO> getCategoryByName(HttpServletRequest request, @PathVariable String name) {
        String endPoint = "/categories/name/" + name;
        CategoryDTO category = categoryService.getCategoryByName(name);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(category, endPoint), HttpStatusCode.valueOf(200));
    }
}
