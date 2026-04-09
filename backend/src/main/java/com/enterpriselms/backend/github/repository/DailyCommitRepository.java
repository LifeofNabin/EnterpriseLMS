package com.enterpriselms.backend.github.repository;

import com.enterpriselms.backend.github.entity.DailyCommit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyCommitRepository extends JpaRepository<DailyCommit, Long> {

    /**
     * Find commit record for a student on a specific date
     */
    Optional<DailyCommit> findByStudentIdAndCommitDate(Long studentId, LocalDate commitDate);

    /**
     * Get all commit records for a student in a date range
     */
    List<DailyCommit> findByStudentIdAndCommitDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);

    /**
     * Get all commit records for a student (all time)
     */
    List<DailyCommit> findByStudentIdOrderByCommitDateDesc(Long studentId);

    /**
     * Get students who DID NOT code on a specific date
     */
    @Query("SELECT DISTINCT d.studentId FROM DailyCommit d WHERE d.commitDate = :date AND d.didCode = false")
    List<Long> findInactiveStudentsByDate(@Param("date") LocalDate date);

    /**
     * Get students who DID code on a specific date
     */
    @Query("SELECT DISTINCT d.studentId FROM DailyCommit d WHERE d.commitDate = :date AND d.didCode = true")
    List<Long> findActiveStudentsByDate(@Param("date") LocalDate date);

    /**
     * Get students at risk (no commits for N days)
     */
    @Query("SELECT d.studentId, MAX(d.commitDate) as lastCommitDate " +
            "FROM DailyCommit d " +
            "WHERE d.didCode = true " +
            "GROUP BY d.studentId " +
            "HAVING MAX(d.commitDate) < :thresholdDate")
    List<Object[]> findStudentsWithNoCommitsSince(@Param("thresholdDate") LocalDate thresholdDate);

    /**
     * Get current streak for a student
     */
    default int getCurrentStreak(Long studentId) {
        LocalDate today = LocalDate.now();
        int streak = 0;
        LocalDate checkDate = today;

        while (true) {
            Optional<DailyCommit> record = findByStudentIdAndCommitDate(studentId, checkDate);
            if (record.isPresent() && record.get().getDidCode()) {
                streak++;
                checkDate = checkDate.minusDays(1);
            } else {
                break;
            }
        }
        return streak;
    }

    /**
     * Get total commits for a student in date range
     */
    @Query("SELECT SUM(d.commitsCount) FROM DailyCommit d " +
            "WHERE d.studentId = :studentId " +
            "AND d.commitDate BETWEEN :startDate AND :endDate")
    Integer getTotalCommitsInRange(@Param("studentId") Long studentId,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);

    /**
     * Get weekly activity summary for a student
     */
    @Query("SELECT d.commitDate, d.commitsCount, d.didCode " +
            "FROM DailyCommit d " +
            "WHERE d.studentId = :studentId " +
            "AND d.commitDate >= :startDate " +
            "ORDER BY d.commitDate ASC")
    List<Object[]> getWeeklyActivity(@Param("studentId") Long studentId,
                                     @Param("startDate") LocalDate startDate);
}