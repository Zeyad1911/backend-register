package com.example.backendregister.services;

import com.example.backendregister.SecurityConfig.ConfirmationToken;
import com.example.backendregister.model.User;
import com.example.backendregister.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class UserService implements UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername  (String username) throws UsernameNotFoundException {
        User userDetails = userRepository.findByEmail(username);
        if(userDetails == null || !userDetails.isEnabled()) {
            throw  new UsernameNotFoundException("User not found");
        }
         return new org.springframework.security.core.userdetails.User(
                userDetails.getUsername(),
                userDetails.getPassword(),
                 Collections.singletonList(
                         new SimpleGrantedAuthority("ROLE_" + userDetails.getRole().name())));
    }
}
