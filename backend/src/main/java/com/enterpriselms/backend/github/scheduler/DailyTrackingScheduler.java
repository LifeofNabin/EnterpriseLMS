package com.enterpriselms.backend.github.scheduler;

import com.enterpriselms.backend.github.service.GitHubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@EnableScheduling
public class DailyTrackingScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DailyTrackingScheduler.class);

    @Autowired
    private GitHubService gitHubService;

    /**
     * Cron job runs at 11:00 PM every day
     * Format: second minute hour day month day-of-week
     * 0 23 * * * = At 23:00 (11:00 PM) every day
     */
    @Scheduled(cron = "0  0 23 * * *", zone = "Asia/Kolkata")
    public void trackAllStudentsAt11PM() {
        logger.info("========================================");
        logger.info("🕚 11 PM CRON JOB STARTING");
        logger.info("Time: {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        logger.info("========================================");

        try {
            // Track all students with GitHub connected
            Map<String, Integer> results = gitHubService.trackAllStudents();

            logger.info("========================================");
            logger.info("✅ CRON JOB COMPLETED");
            logger.info("Total students tracked: {}", results.get("total"));
            logger.info("Successful: {}", results.get("success"));
            logger.info("Failed: {}", results.get("failure"));
            logger.info("Completion time: {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            logger.info("========================================");

            // Optional: Send summary email to admin
            // sendSummaryEmail(results);

        } catch (Exception e) {
            logger.error("❌ CRON JOB FAILED: {}", e.getMessage(), e);
        }
    }

    /**
     * For testing purposes - run every minute (comment out in production)
     * Use this to test without waiting for 11 PM
     */
    // @Scheduled(cron = "0 * * * * *", zone = "Asia/Kolkata")  // Every minute for testing
    public void testTracking() {
        logger.info("🧪 TEST MODE: Tracking all students");
        Map<String, Integer> results = gitHubService.trackAllStudents();
        logger.info("Test results: {} success, {} failure", results.get("success"), results.get("failure"));
    }

    /**
     * Health check - runs every hour to verify scheduler is alive
     */
    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Kolkata")
    public void healthCheck() {
        logger.debug("✅ Scheduler is alive and running at: {}",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}