package com.enterpriselms.backend.github.repository;

import com.enterpriselms.backend.github.entity.GitHubToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GitHubTokenRepository extends JpaRepository<GitHubToken, Long> {

    /**
     * Find token by student ID
     */
    Optional<GitHubToken> findByStudentId(Long studentId);

    /**
     * Check if student has connected GitHub
     */
    boolean existsByStudentIdAndIsConnectedTrue(Long studentId);

    /**
     * Find all connected students
     */
    List<GitHubToken> findByIsConnectedTrue();

    /**
     * Find token by GitHub username
     */
    Optional<GitHubToken> findByGithubUsername(String githubUsername);

    /**
     * Update last verified timestamp
     */
    @Modifying
    @Transactional
    @Query("UPDATE GitHubToken t SET t.lastVerifiedAt = :verifiedAt WHERE t.studentId = :studentId")
    void updateLastVerifiedAt(@Param("studentId") Long studentId, @Param("verifiedAt") LocalDateTime verifiedAt);

    /**
     * Disconnect GitHub for a student
     */
    @Modifying
    @Transactional
    @Query("UPDATE GitHubToken t SET t.isConnected = false, t.updatedAt = :now WHERE t.studentId = :studentId")
    void disconnectByStudentId(@Param("studentId") Long studentId, @Param("now") LocalDateTime now);

    /**
     * Delete expired tokens
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM GitHubToken t WHERE t.tokenExpiresAt < :now")
    int deleteExpiredTokens(@Param("now") LocalDateTime now);

    /**
     * Find all tokens that need verification (older than given date)
     */
    @Query("SELECT t FROM GitHubToken t WHERE t.isConnected = true AND (t.lastVerifiedAt IS NULL OR t.lastVerifiedAt < :since)")
    List<GitHubToken> findTokensNeedingVerification(@Param("since") LocalDateTime since);
}