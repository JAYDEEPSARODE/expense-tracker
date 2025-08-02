package com.expensetracker.expenseService.userService;

import com.expensetracker.expenseService.entity.User;

import java.util.Optional;

public interface UserService {

    User register(User user);

    Optional<User> login(String email, String passWord);
}
