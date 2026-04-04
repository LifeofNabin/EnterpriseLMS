package com.enterpriselms.backend.user.dto;

import com.enterpriselms.backend.user.enums.UserRole;
import com.enterpriselms.backend.user.enums.UserStatus;
import com.enterpriselms.backend.user.enums.StudentType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private UserRole role;
    private StudentType studentType;
    private UserStatus status;
    private String instituteName;
    private String courseInterest;
    private LocalDateTime createdAt;
}