package com.expensetracker.expenseService.userService;

import com.expensetracker.expenseService.entity.User;
import com.expensetracker.expenseService.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    @SneakyThrows
    public User register(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new BadRequestException("Email is Already Register please use another One");
        return userRepository.save(user);
    }

    @Override
    @SneakyThrows
    public Optional<User> login(String email, String passWord) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty())
                throw new BadRequestException("User Not Found Please Register");
            else {
                return user.filter(u -> u.getPassword().equals(passWord));
//                return user;
            }
        } catch (Exception e) {
//            log.info("Login error: {}", e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
    }
}
