package com.example.backendregister.controller;

import com.example.backendregister.Resonses.LoginResponse;
import com.example.backendregister.model.Role;
import com.example.backendregister.model.User;
import com.example.backendregister.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user){
        try {
            userService.saveUser(user);
            try {
                String roleString = user.getRole().toString();
                String formattedRoleString = roleString.substring(0, 1).toUpperCase() + roleString.substring(1).toLowerCase();
                user.setRole(Role.valueOf(formattedRoleString));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role: " + user.getRole());
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "An error occurred while creating the user.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", "An error occurred while creating the user.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetails);
//            User LoggedInUser = (User) userDetails;
            LoginResponse loginResponse =
                    new LoginResponse
                            ("User logged in successfully",userDetails.getUsername(),
                                    userDetails.getAuthorities().toString());
            return ResponseEntity.ok(loginResponse);

        }
        catch (AuthenticationException e) {
            LoginResponse errorResponse = new LoginResponse(
                    "Login failed: " + e.getMessage(),
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
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