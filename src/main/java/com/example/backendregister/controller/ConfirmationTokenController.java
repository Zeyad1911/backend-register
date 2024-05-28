package com.example.backendregister.controller;

import com.example.backendregister.Repository.ConfirmationTokenRepository;
import com.example.backendregister.SecurityConfig.ConfirmationToken;
import com.example.backendregister.services.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping ("/token")
public class ConfirmationTokenController {

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @GetMapping("/confirm")
    public ModelAndView ConfirmToken(@RequestParam("token") String token){
            confirmationTokenService.confirmToken(token);
            return new ModelAndView("redirect:/confirmation-page.html");
    }
}
