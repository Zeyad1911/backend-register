package com.example.backendregister.services;

import com.example.backendregister.Repository.ConfirmationTokenRepository;
import com.example.backendregister.SecurityConfig.ConfirmationToken;
import com.example.backendregister.model.User;
import com.example.backendregister.model.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserRepository userRepository;




    public void saveToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = getToken(token).orElseThrow(
                ()-> new IllegalStateException("Token can not be found")
        );


        if (confirmationToken.getConfirmedAt() != null) {
            return "Token already confirmed";
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return "Token has expired";
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        saveToken(confirmationToken);

        User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        return "Token confirmed and user enabled";
    }

    public Optional<ConfirmationToken> getToken(String token){
       return confirmationTokenRepository.findByToken(token);
    }


    public void deleteToken(ConfirmationToken token){
        confirmationTokenRepository.delete(token);
    }



}
