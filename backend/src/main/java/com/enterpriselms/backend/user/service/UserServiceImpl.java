package com.enterpriselms.backend.user.service;

import com.enterpriselms.backend.user.dto.UserRegistrationDTO;
import com.enterpriselms.backend.user.dto.UserResponseDTO;
import com.enterpriselms.backend.user.entity.User;
import com.enterpriselms.backend.user.enums.StudentType;
import com.enterpriselms.backend.user.enums.UserRole;
import com.enterpriselms.backend.user.enums.UserStatus;
import com.enterpriselms.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO registerWebsiteStudent(UserRegistrationDTO registrationDTO) {
        // Check if email already exists
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(registrationDTO.getPassword()); // Will encrypt later
        user.setFullName(registrationDTO.getFullName());
        user.setPhone(registrationDTO.getPhone());
        user.setRole(UserRole.STUDENT);
        user.setStudentType(StudentType.WEBSITE);
        user.setStatus(UserStatus.PENDING); // Website students start as pending
        user.setCourseInterest(registrationDTO.getCourseInterest());
        user.setIsActive(true);

        // Save to database
        User savedUser = userRepository.save(user);

        // Return DTO
        return convertToDTO(savedUser);
    }

    @Override
    public UserResponseDTO createBulkUser(UserRegistrationDTO registrationDTO) {
        // Check if email already exists
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(registrationDTO.getPassword()); // Will encrypt later
        user.setFullName(registrationDTO.getFullName());
        user.setPhone(registrationDTO.getPhone());
        user.setRole(UserRole.STUDENT);
        user.setStudentType(StudentType.BULK);
        user.setStatus(UserStatus.ACTIVE); // Bulk students are active immediately
        user.setInstituteName(registrationDTO.getInstituteName());
        user.setIsActive(true);

        // Save to database
        User savedUser = userRepository.save(user);

        // Return DTO
        return convertToDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    @Override
    public List<UserResponseDTO> getPendingUsers() {
        return userRepository.findByStatus(UserStatus.PENDING)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUserStatus(Long userId, String status, String rejectionReason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.valueOf(status));

        // If rejected, you might want to store rejection reason somewhere
        // For now, we just update status

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Override
    public UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setStudentType(user.getStudentType());
        dto.setStatus(user.getStatus());
        dto.setInstituteName(user.getInstituteName());
        dto.setCourseInterest(user.getCourseInterest());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}