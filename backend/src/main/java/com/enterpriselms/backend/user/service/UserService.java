package com.enterpriselms.backend.user.service;

import com.enterpriselms.backend.user.dto.UserRegistrationDTO;
import com.enterpriselms.backend.user.dto.UserResponseDTO;
import com.enterpriselms.backend.user.entity.User;
import java.util.List;

public interface UserService {

    // Register a new user (website registration)
    UserResponseDTO registerWebsiteStudent(UserRegistrationDTO registrationDTO);

    // Create bulk user (admin import)
    UserResponseDTO createBulkUser(UserRegistrationDTO registrationDTO);

    // Get user by ID
    UserResponseDTO getUserById(Long id);

    // Get user by email
    UserResponseDTO getUserByEmail(String email);

    // Get all pending users (for admin)
    List<UserResponseDTO> getPendingUsers();

    // Update user status (approve/reject)
    UserResponseDTO updateUserStatus(Long userId, String status, String rejectionReason);

    // Helper method to convert Entity to DTO
    UserResponseDTO convertToDTO(User user);
}