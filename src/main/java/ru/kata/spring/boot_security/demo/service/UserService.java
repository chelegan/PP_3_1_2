package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.model.User;
import java.util.List;

public interface UserService {
    List<User> getListOfAllUsers();
    void deleteUser(Long id);
    void updateUser(User user);
    boolean addUser(User user);
    User getUser(Long id);
    Long getIdByUsername(String username);
}
