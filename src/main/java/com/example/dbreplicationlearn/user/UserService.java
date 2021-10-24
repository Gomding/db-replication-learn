package com.example.dbreplicationlearn.user;

import com.example.dbreplicationlearn.domain.User;
import com.example.dbreplicationlearn.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        System.out.println("execute save");
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        System.out.println("execute findAll");
        return userRepository.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.NESTED)
    public User findById(Long id) {
        System.out.println("execute findById");
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void update(Long id, String name) {
        User user = findById(id);
        user.update(name);
    }
}
