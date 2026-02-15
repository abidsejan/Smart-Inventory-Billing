package com.inventory.service;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthenticationService {

    private final UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean register(String username, String password, String role) {
        // Check if user already exists
        if (userDAO.findByUsername(username) != null) {
            return false; // User already exists
        }
        
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // Pass plain-text password to DAO
        newUser.setRole(role != null ? role : "Staff"); // Default to "Staff" if role is not provided
        
        return userDAO.save(newUser);
    }
}

