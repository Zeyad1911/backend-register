//package com.example.backendregister.SecurityConfig;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
////        http.csrf().disable().
////        authorizeHttpRequests(
////                authorize -> authorize.requestMatchers("/api/users/register").permitAll()
////                        .anyRequest().authenticated()
////
////        );
////        return http.build();
////    }
//
//}