package com.expensetracker.expenseService.userController;

import com.expensetracker.expenseService.entity.User;
import com.expensetracker.expenseService.global.ResponseDTO;
import com.expensetracker.expenseService.userService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    private PasswordEncoder passwordEncoder;

    @GetMapping(path = "/getUserDetails")
    public String getUserDetails(){
        return "User name is Jaydeep";
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(HttpServletRequest httpServletRequest,
                                   @RequestBody User user){
        String endPoint = "/login";
        Timestamp landingTime = Timestamp.valueOf(LocalDateTime.now());
        System.out.println(landingTime);
        return new ResponseEntity<>(new ResponseDTO(userService.login(user.getEmail(),user.getPassword()),landingTime,endPoint), HttpStatus.OK);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> register(HttpServletRequest httpServletRequest,
                                   @RequestBody User user){
        String endpoint = "/register";
        Timestamp landingTime = Timestamp.valueOf(LocalDateTime.now());
        System.out.println(landingTime);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(new ResponseDTO(userService.register(user),landingTime,endpoint), HttpStatus.OK);
    }


}
