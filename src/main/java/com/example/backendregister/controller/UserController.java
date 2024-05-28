package com.example.backendregister.controller;

import com.example.backendregister.model.User;
import com.example.backendregister.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void createUser(@RequestBody User user){
        if(user.getPassword() == null){
            return;
        }
        userService.saveUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,@RequestBody User userDetails) {
        User user = userService.getUserById(id);
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setEmail(userDetails.getEmail());
        userService.saveUser(user);

        return user;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}