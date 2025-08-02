package com.expensetracker.expenseService.userController;


import com.expensetracker.expenseService.entity.User;
import com.expensetracker.expenseService.global.ResponseDTO;
import com.expensetracker.expenseService.userService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.expensetracker.expenseService.security.JwtRequest;
import com.expensetracker.expenseService.security.JwtResponse;
import com.expensetracker.expenseService.security.JwtUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponse(token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }


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
