package com.enterpriselms.backend.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()

                        // ========== GITHUB AGENT ENDPOINTS (ADD THESE) ==========
                        .requestMatchers("/api/github/auth-url").permitAll()
                        .requestMatchers("/api/github/callback").permitAll()
                        .requestMatchers("/api/github/connect").permitAll()
                        .requestMatchers("/api/github/status/**").permitAll()
                        .requestMatchers("/api/github/streak/**").permitAll()
                        .requestMatchers("/api/github/activity/**").permitAll()
                        .requestMatchers("/api/github/at-risk").permitAll()
                        .requestMatchers("/api/github/stats/**").permitAll()
                        .requestMatchers("/api/github/profile/**").permitAll()
                        .requestMatchers("/api/github/track/**").permitAll()
                        // ========================================================

                        // Role-based endpoints (to be implemented by team)
                        .requestMatchers("/api/student/**").hasRole("STUDENT")
                        .requestMatchers("/api/tutor/**").hasRole("TUTOR")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                );

        return http.build();
    }
}