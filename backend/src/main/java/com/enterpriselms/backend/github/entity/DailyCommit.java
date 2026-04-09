package com.enterpriselms.backend.github.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_commits")
public class DailyCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "commit_date", nullable = false)
    private LocalDate commitDate;

    @Column(name = "commits_count", nullable = false)
    private Integer commitsCount = 0;

    @Column(name = "did_code", nullable = false)
    private Boolean didCode = false;

    @Column(name = "streak", nullable = false)
    private Integer streak = 0;

    @Column(name = "last_commit_time")
    private LocalDateTime lastCommitTime;

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt;

    // Constructors
    public DailyCommit() {}

    public DailyCommit(Long studentId, LocalDate commitDate) {
        this.studentId = studentId;
        this.commitDate = commitDate;
        this.checkedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDate getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(LocalDate commitDate) {
        this.commitDate = commitDate;
    }

    public Integer getCommitsCount() {
        return commitsCount;
    }

    public void setCommitsCount(Integer commitsCount) {
        this.commitsCount = commitsCount;
        this.didCode = commitsCount > 0;
    }

    public Boolean getDidCode() {
        return didCode;
    }

    public void setDidCode(Boolean didCode) {
        this.didCode = didCode;
    }

    public Integer getStreak() {
        return streak;
    }

    public void setStreak(Integer streak) {
        this.streak = streak;
    }

    public LocalDateTime getLastCommitTime() {
        return lastCommitTime;
    }

    public void setLastCommitTime(LocalDateTime lastCommitTime) {
        this.lastCommitTime = lastCommitTime;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }
}