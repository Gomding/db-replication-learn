package com.example.dbreplicationlearn.controller;

import com.example.dbreplicationlearn.domain.User;
import com.example.dbreplicationlearn.user.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void createUser(String name) {
        userService.save(new User(name));
    }

    public void toDoSomthing(Long id, String name) {
        User findUser = userService.findById(id);
        userService.update(findUser.getId(), name);
    }
}
