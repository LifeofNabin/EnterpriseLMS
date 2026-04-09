package com.enterpriselms.backend.github.controller;

import com.enterpriselms.backend.github.entity.DailyCommit;
import com.enterpriselms.backend.github.entity.GitHubToken;
import com.enterpriselms.backend.github.service.GitHubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/github")
@CrossOrigin(origins = "http://localhost:3000")
public class GitHubController {

    private static final Logger logger = LoggerFactory.getLogger(GitHubController.class);

    @Autowired
    private GitHubService gitHubService;

    // ==================== OAUTH ENDPOINTS ====================

    @GetMapping("/auth-url")
    public ResponseEntity<Map<String, String>> getAuthUrl(@RequestParam(value = "studentId", required = false) Long studentId) {
        String authUrl = gitHubService.getGitHubAuthUrl(studentId != null ? studentId : 1L);
        Map<String, String> response = new HashMap<>();
        response.put("url", authUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/callback")
    public void handleCallback(@RequestParam("code") String code,
                               @RequestParam(value = "state", required = false) String state,
                               @RequestParam(value = "studentId", required = false) Long studentId,
                               HttpServletResponse response,
                               HttpServletRequest request) throws IOException {
        try {
            // If studentId not in URL, use default (1)
            if (studentId == null) {
                studentId = 1L;
                logger.info("No studentId in request, using default: {}", studentId);
            }

            logger.info("Processing GitHub callback for student: {}", studentId);

            // Exchange code for token and save to database
            GitHubToken token = gitHubService.exchangeCodeForToken(studentId, code);

            // Redirect back to frontend with success
            String frontendUrl = "http://localhost:3000/dashboard?github=connected&username=" + token.getGithubUsername();
            response.sendRedirect(frontendUrl);

        } catch (Exception e) {
            logger.error("GitHub callback failed: {}", e.getMessage());
            String frontendUrl = "http://localhost:3000/dashboard?github=error&message=" + e.getMessage();
            response.sendRedirect(frontendUrl);
        }
    }

    // ==================== CONNECTION MANAGEMENT ====================

    @PostMapping("/connect")
    public ResponseEntity<Map<String, Object>> connectGitHub(@RequestBody Map<String, Object> request) {
        Long studentId = Long.valueOf(request.get("studentId").toString());
        String code = request.get("code").toString();

        try {
            GitHubToken token = gitHubService.exchangeCodeForToken(studentId, code);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "GitHub connected successfully");
            response.put("githubUsername", token.getGithubUsername());
            response.put("connectedAt", token.getConnectedAt());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to connect GitHub: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/disconnect/{studentId}")
    public ResponseEntity<Map<String, Object>> disconnectGitHub(@PathVariable Long studentId) {
        try {
            gitHubService.disconnectGitHub(studentId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "GitHub disconnected successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to disconnect: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/status/{studentId}")
    public ResponseEntity<Map<String, Object>> getConnectionStatus(@PathVariable Long studentId) {
        boolean isConnected = gitHubService.isGitHubConnected(studentId);
        Optional<String> githubUsername = gitHubService.getGitHubUsername(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("connected", isConnected);
        response.put("studentId", studentId);

        if (githubUsername.isPresent()) {
            response.put("githubUsername", githubUsername.get());
        }

        return ResponseEntity.ok(response);
    }

    // ==================== TRACKING & ACTIVITY ENDPOINTS ====================

    @GetMapping("/streak/{studentId}")
    public ResponseEntity<Map<String, Object>> getStreak(@PathVariable Long studentId) {
        int streak = gitHubService.getCurrentStreak(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("streak", streak);
        response.put("lastUpdated", java.time.LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/activity/weekly/{studentId}")
    public ResponseEntity<Map<String, Object>> getWeeklyActivity(@PathVariable Long studentId) {
        List<Map<String, Object>> weeklyActivity = gitHubService.getWeeklyActivity(studentId);
        int totalCommits = gitHubService.getTotalCommitsThisWeek(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("weeklyActivity", weeklyActivity);
        response.put("totalCommitsThisWeek", totalCommits);
        response.put("currentStreak", gitHubService.getCurrentStreak(studentId));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/activity/range/{studentId}")
    public ResponseEntity<Map<String, Object>> getActivityInRange(
            @PathVariable Long studentId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<Map<String, Object>> weeklyActivity = gitHubService.getWeeklyActivity(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("activity", weeklyActivity);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/track/{studentId}")
    public ResponseEntity<Map<String, Object>> trackStudentManually(@PathVariable Long studentId) {
        try {
            DailyCommit commit = gitHubService.fetchAndSaveTodayCommits(studentId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("studentId", studentId);

            if (commit != null) {
                response.put("commitsCount", commit.getCommitsCount());
                response.put("didCode", commit.getDidCode());
                response.put("streak", commit.getStreak());
            } else {
                response.put("message", "Student not connected to GitHub");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Tracking failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ==================== REPORTING & ANALYTICS ENDPOINTS ====================

    @GetMapping("/at-risk")
    public ResponseEntity<Map<String, Object>> getAtRiskStudents() {
        List<Map<String, Object>> atRiskStudents = gitHubService.getAtRiskStudents();

        Map<String, Object> response = new HashMap<>();
        response.put("count", atRiskStudents.size());
        response.put("students", atRiskStudents);
        response.put("generatedAt", java.time.LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/platform")
    public ResponseEntity<Map<String, Object>> getPlatformStats() {
        Map<String, Object> stats = gitHubService.getPlatformStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/profile/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentProfile(@PathVariable Long studentId) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("studentId", studentId);
        profile.put("isConnected", gitHubService.isGitHubConnected(studentId));

        if (gitHubService.isGitHubConnected(studentId)) {
            profile.put("githubUsername", gitHubService.getGitHubUsername(studentId).orElse(null));
            profile.put("currentStreak", gitHubService.getCurrentStreak(studentId));
            profile.put("totalCommitsThisWeek", gitHubService.getTotalCommitsThisWeek(studentId));
            profile.put("weeklyActivity", gitHubService.getWeeklyActivity(studentId));
        }

        profile.put("generatedAt", java.time.LocalDateTime.now());

        return ResponseEntity.ok(profile);
    }

    // ==================== BULK OPERATIONS ====================

    @PostMapping("/track-all")
    public ResponseEntity<Map<String, Object>> trackAllStudents() {
        logger.info("Manual trigger: Tracking all students");
        Map<String, Integer> results = gitHubService.trackAllStudents();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("totalStudents", results.get("total"));
        response.put("successCount", results.get("success"));
        response.put("failureCount", results.get("failure"));
        response.put("timestamp", java.time.LocalDateTime.now());

        return ResponseEntity.ok(response);
    }
}