package com.expensetracker.expenseService.expenseController;

import com.expensetracker.expenseService.ExpenseService.ExpenseService;
import com.expensetracker.expenseService.Utility.ResponseBuilder;
import com.expensetracker.expenseService.entity.ExpenseEntity;
import com.expensetracker.expenseService.global.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @PostMapping("/addExpense")
    public ResponseEntity<ResponseDTO> addExpense(HttpServletRequest httpServletRequest, @RequestBody ExpenseEntity request){
        String endPoint = "/addExpense";
        Object response = expenseService.addExpense(request);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(response, endPoint), HttpStatusCode.valueOf(200));
    }
}
