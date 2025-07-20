package com.expensetracker.userservice.userService;

import com.expensetracker.userservice.entity.User;

import java.util.Optional;

public interface UserService {

    User register(User user);

    Optional<User> login(String email, String passWord);
}
