package com.example.dbreplicationlearn;

import com.example.dbreplicationlearn.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    UserController userController;

    @Test
    void create() {
        userController.createUser("charlie");
    }

    @Test
    void test() {
        userController.toDoSomthing(1L, "JPA student1");
    }
}
