package com.example.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.mvc.imple.UserDaoImpl;
import com.example.mvc.model.User;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    private static final String ADMIN = "admin";
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "admin";
    
    private final UserDaoImpl userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserDaoImpl userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        User adminUser = userDAO.getUserByEmail(ADMIN_EMAIL);
        if (adminUser == null) {
            adminUser = new User();
            adminUser.setName(ADMIN);
            adminUser.setEmail(ADMIN_EMAIL);
            adminUser.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            adminUser.setRole(ADMIN);
            userDAO.saveUser(adminUser);
        }
    }
}
