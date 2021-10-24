package com.example.dbreplicationlearn;

import com.example.dbreplicationlearn.domain.User;
import com.example.dbreplicationlearn.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    EntityManager entityManager;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User("charlie");
    }

    @DisplayName("유저를 저장한다.")
    @Test
    void save() {
        userService.save(user1);
    }

    @DisplayName("유저 전체를 조회한다.")
    @Test
    void findAll() {
        List<User> users = userService.findAll();
        System.out.println("User count = " + users.size());
        users.forEach(user -> System.out.println(user.getName()));
    }

    @DisplayName("N개의 Slave 데이터베이스를 번갈아가며 사용한다.")
    @Test
    void switchSlaveDatabase() {
        User newUser = userService.save(user1);

        User findUser1 = userService.findById(newUser.getId());
        User findUser2 = userService.findById(newUser.getId());
        User findUser3 = userService.findById(newUser.getId());
        User findUser4 = userService.findById(newUser.getId());
        User findUser5 = userService.findById(newUser.getId());

        assertThat(findUser1.getName()).isEqualTo(user1.getName());
    }

    @DisplayName("유저를 수정한다.")
    @Test
    void update() {
        // given
        User new_charlie = userService.save(new User("new charlie"));
        entityManager.clear();
        userService.update(new_charlie.getId(), "updated charlie");
    }
}
