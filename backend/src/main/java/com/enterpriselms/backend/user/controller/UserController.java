package com.enterpriselms.backend.user.controller;

import com.enterpriselms.backend.user.dto.UserRegistrationDTO;
import com.enterpriselms.backend.user.dto.UserResponseDTO;
import com.enterpriselms.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Public endpoint for website student registration
    @PostMapping("/register/website")
    public ResponseEntity<UserResponseDTO> registerWebsiteStudent(
            @Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO response = userService.registerWebsiteStudent(registrationDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Admin endpoint for bulk student creation (will protect with security later)
    @PostMapping("/bulk")
    public ResponseEntity<UserResponseDTO> createBulkUser(
            @Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO response = userService.createBulkUser(registrationDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    // Get pending users (admin only - will secure later)
    @GetMapping("/pending")
    public ResponseEntity<List<UserResponseDTO>> getPendingUsers() {
        List<UserResponseDTO> pendingUsers = userService.getPendingUsers();
        return ResponseEntity.ok(pendingUsers);
    }

    // Approve or reject user (admin only)
    @PutMapping("/{id}/status")
    public ResponseEntity<UserResponseDTO> updateUserStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String rejectionReason) {
        UserResponseDTO response = userService.updateUserStatus(id, status, rejectionReason);
        return ResponseEntity.ok(response);
    }
}