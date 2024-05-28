package com.example.backendregister.services;

import com.example.backendregister.SecurityConfig.ConfirmationToken;
import com.example.backendregister.model.User;
import com.example.backendregister.model.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public String saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken Confirmationtoken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveToken(Confirmationtoken);
        enableUser(Confirmationtoken.getUser());

        //send:Email
        String link = "http://localhost:8081/token/confirm?token=" + token;
        emailService.sendMail(
                user.getEmail(),"confirm your email registering at electrofix application",link);
        return token;

    }


    public void enableUser(User user){
        user.setEnabled(true);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteAllUsers(){
        System.out.println("All users has been deleted");
        userRepository.deleteAll();
    }

    public User getUserById(Long Id) {
        return userRepository.findById(Id).orElse(null);
    }
}
