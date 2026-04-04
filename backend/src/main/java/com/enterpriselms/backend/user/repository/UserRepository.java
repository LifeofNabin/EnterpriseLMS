package com.enterpriselms.backend.user.repository;

import com.enterpriselms.backend.user.entity.User;
import com.enterpriselms.backend.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used in login)
    Optional<User> findByEmail(String email);

    // Check if email exists (used in registration)
    boolean existsByEmail(String email);

    // Find users by status (for admin pending approvals)
    List<User> findByStatus(UserStatus status);

    // Find users by role
    List<User> findByRole(String role);
}