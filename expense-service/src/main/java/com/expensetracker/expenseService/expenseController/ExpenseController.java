package com.expensetracker.expenseService.expenseController;

import com.expensetracker.expenseService.ExpenseService.ExpenseService;
import com.expensetracker.expenseService.Utility.ResponseBuilder;
import com.expensetracker.expenseService.entity.ExpenseEntity;
import com.expensetracker.expenseService.global.ResponseDTO;
import com.expensetracker.expenseService.service.RedisCacheMaster;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @Autowired
    RedisCacheMaster redisCacheMaster;

    @PostMapping("/addExpense")
    public ResponseEntity<ResponseDTO> addExpense(HttpServletRequest httpServletRequest, @RequestBody ExpenseEntity request){
        String endPoint = "/addExpense";
        String userEmail = httpServletRequest.getHeader("X-User-Email");
        if (userEmail != null) {
            request.setUserEmail(userEmail);
        }
        Object response = expenseService.addExpense(request);
        // Clear cache for this user
        redisCacheMaster.delete("getExpenses_" + request.getUserEmail());
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(response, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/getExpenses/{userEmail}")
    public ResponseEntity<ResponseDTO> getExpenseDetails(HttpServletRequest httpServletRequest, @PathVariable("userEmail") String userEmail){
        String endPoint = "/getExpenses";
        String cacheKey = "getExpenses_" + userEmail;
        if(redisCacheMaster.exists(cacheKey)){
            return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(redisCacheMaster.getValue(cacheKey), endPoint), HttpStatusCode.valueOf(200));
        }
        Object response = expenseService.getExpenseDetails(userEmail);
        redisCacheMaster.saveValue(cacheKey, response, 300); // Cache for 5 minutes
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(response, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ResponseDTO> getExpenseById(HttpServletRequest httpServletRequest, @PathVariable("expenseId") Long expenseId){
        String endPoint = "/getExpenseById";
        String userEmail = httpServletRequest.getHeader("X-User-Email");
        Object response = expenseService.getExpenseById(expenseId, userEmail);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(response, endPoint), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ResponseDTO> updateExpense(HttpServletRequest httpServletRequest, 
                                                      @PathVariable("expenseId") Long expenseId,
                                                      @RequestBody ExpenseEntity request){
        String endPoint = "/updateExpense";
        String userEmail = httpServletRequest.getHeader("X-User-Email");
        Object response = expenseService.updateExpense(expenseId, request, userEmail);
        // Clear cache
        redisCacheMaster.delete("getExpenses_" + userEmail);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(response, endPoint), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ResponseDTO> deleteExpense(HttpServletRequest httpServletRequest, @PathVariable("expenseId") Long expenseId){
        String endPoint = "/deleteExpense";
        String userEmail = httpServletRequest.getHeader("X-User-Email");
        expenseService.deleteExpense(expenseId, userEmail);
        // Clear cache
        redisCacheMaster.delete("getExpenses_" + userEmail);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("Expense deleted successfully", endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseDTO> getExpensesByCategory(HttpServletRequest httpServletRequest, 
                                                               @PathVariable("category") String category){
        String endPoint = "/getExpensesByCategory";
        String userEmail = httpServletRequest.getHeader("X-User-Email");
        Object response = expenseService.getExpensesByCategory(userEmail, category);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(response, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/date-range")
    public ResponseEntity<ResponseDTO> getExpensesByDateRange(HttpServletRequest httpServletRequest,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        String endPoint = "/getExpensesByDateRange";
        String userEmail = httpServletRequest.getHeader("X-User-Email");
        Object response = expenseService.getExpensesByDateRange(userEmail, startDate, endDate);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(response, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/category/{category}/date-range")
    public ResponseEntity<ResponseDTO> getExpensesByCategoryAndDateRange(HttpServletRequest httpServletRequest,
                                                                          @PathVariable("category") String category,
                                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        String endPoint = "/getExpensesByCategoryAndDateRange";
        String userEmail = httpServletRequest.getHeader("X-User-Email");
        Object response = expenseService.getExpensesByCategoryAndDateRange(userEmail, category, startDate, endDate);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(response, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/total")
    public ResponseEntity<ResponseDTO> getTotalExpenses(HttpServletRequest httpServletRequest){
        String endPoint = "/getTotalExpenses";
        String userEmail = httpServletRequest.getHeader("X-User-Email");
        Double total = expenseService.getTotalExpensesByUser(userEmail);
        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        response.put("userEmail", userEmail);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(response, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/total/date-range")
    public ResponseEntity<ResponseDTO> getTotalExpensesByDateRange(HttpServletRequest httpServletRequest,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        String endPoint = "/getTotalExpensesByDateRange";
        String userEmail = httpServletRequest.getHeader("X-User-Email");
        Double total = expenseService.getTotalExpensesByUserAndDateRange(userEmail, startDate, endDate);
        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        response.put("userEmail", userEmail);
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(response, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/total/category/{category}")
    public ResponseEntity<ResponseDTO> getTotalExpensesByCategory(HttpServletRequest httpServletRequest,
                                                                  @PathVariable("category") String category){
        String endPoint = "/getTotalExpensesByCategory";
        String userEmail = httpServletRequest.getHeader("X-User-Email");
        Double total = expenseService.getTotalExpensesByCategory(userEmail, category);
        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        response.put("userEmail", userEmail);
        response.put("category", category);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(response, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/redis/sample")
    public ResponseEntity<String> redisSampleApi() {
        if (redisCacheMaster.exists("sampleKey")) {
            log.info("Data found in Redis for key 'sampleKey'");
            return ResponseEntity.ok("Data exists in Redis: " + redisCacheMaster.getValue("sampleKey"));
        } else {
            redisCacheMaster.saveValue("sampleKey","Hello from Redis!");
            return ResponseEntity.ok("Data was not present. Now saved to Redis.");
        }
    }
}
