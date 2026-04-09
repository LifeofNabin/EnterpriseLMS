package com.enterpriselms.backend.github.service;

import com.enterpriselms.backend.github.entity.DailyCommit;
import com.enterpriselms.backend.github.entity.GitHubToken;
import com.enterpriselms.backend.github.repository.DailyCommitRepository;
import com.enterpriselms.backend.github.repository.GitHubTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubService.class);

    @Autowired
    private GitHubTokenRepository gitHubTokenRepository;

    @Autowired
    private DailyCommitRepository dailyCommitRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${github.client.id}")
    private String githubClientId;

    @Value("${github.client.secret}")
    private String githubClientSecret;

    @Value("${github.redirect.uri}")
    private String githubRedirectUri;

    private static final String GITHUB_API_BASE = "https://api.github.com";

    // ==================== GITHUB OAUTH METHODS ====================

    /**
     * Generate GitHub OAuth URL for user to authorize
     */
    public String getGitHubAuthUrl(Long studentId) {
        String redirectUriWithStudentId = githubRedirectUri + "?studentId=" + studentId;

        return UriComponentsBuilder.fromHttpUrl("https://github.com/login/oauth/authorize")
                .queryParam("client_id", githubClientId)
                .queryParam("redirect_uri", redirectUriWithStudentId)
                .queryParam("scope", "repo,user:email")
                .queryParam("state", generateState())
                .build()
                .toUriString();
    }

    /**
     * Exchange code for access token and save to database
     */
    public GitHubToken exchangeCodeForToken(Long studentId, String code) {
        try {
            // Exchange code for access token
            String accessToken = getAccessTokenFromCode(code);

            // Get GitHub user info
            Map<String, Object> userInfo = getGitHubUserInfo(accessToken);
            String githubUsername = (String) userInfo.get("login");

            // Save or update token
            Optional<GitHubToken> existingToken = gitHubTokenRepository.findByStudentId(studentId);
            GitHubToken token;

            if (existingToken.isPresent()) {
                token = existingToken.get();
                token.setAccessToken(accessToken);
                token.setGithubUsername(githubUsername);
                token.setIsConnected(true);
                token.setConnectedAt(LocalDateTime.now());
                token.setUpdatedAt(LocalDateTime.now());
            } else {
                token = new GitHubToken(studentId, accessToken, githubUsername);
            }

            return gitHubTokenRepository.save(token);

        } catch (Exception e) {
            logger.error("Failed to exchange code for token: {}", e.getMessage());
            throw new RuntimeException("GitHub authentication failed", e);
        }
    }

    /**
     * Disconnect GitHub for a student
     */
    public void disconnectGitHub(Long studentId) {
        gitHubTokenRepository.disconnectByStudentId(studentId, LocalDateTime.now());
        logger.info("Disconnected GitHub for student: {}", studentId);
    }

    /**
     * Check if student has GitHub connected
     */
    public boolean isGitHubConnected(Long studentId) {
        return gitHubTokenRepository.existsByStudentIdAndIsConnectedTrue(studentId);
    }

    /**
     * Get GitHub username for a student
     */
    public Optional<String> getGitHubUsername(Long studentId) {
        return gitHubTokenRepository.findByStudentId(studentId)
                .filter(GitHubToken::getIsConnected)
                .map(GitHubToken::getGithubUsername);
    }

    // ==================== COMMIT TRACKING METHODS ====================

    /**
     * Fetch today's commits for a student and save to database
     */
    public DailyCommit fetchAndSaveTodayCommits(Long studentId) {
        Optional<GitHubToken> tokenOpt = gitHubTokenRepository.findByStudentId(studentId);
        if (tokenOpt.isEmpty() || !tokenOpt.get().getIsConnected()) {
            logger.warn("Student {} does not have GitHub connected", studentId);
            return null;
        }

        GitHubToken token = tokenOpt.get();
        LocalDate today = LocalDate.now();

        try {
            // Fetch commits from GitHub
            CommitData commitData = fetchCommitsForDate(token.getGithubUsername(), token.getAccessToken(), today);

            // Get yesterday's record for streak calculation
            LocalDate yesterday = today.minusDays(1);
            Optional<DailyCommit> yesterdayRecord = dailyCommitRepository.findByStudentIdAndCommitDate(studentId, yesterday);
            int yesterdayStreak = yesterdayRecord.map(DailyCommit::getStreak).orElse(0);

            // Calculate new streak
            int newStreak = commitData.didCode ? yesterdayStreak + 1 : 0;

            // Save or update daily record
            Optional<DailyCommit> existingRecord = dailyCommitRepository.findByStudentIdAndCommitDate(studentId, today);
            DailyCommit dailyCommit;

            if (existingRecord.isPresent()) {
                dailyCommit = existingRecord.get();
                dailyCommit.setCommitsCount(commitData.commitsCount);
                dailyCommit.setDidCode(commitData.didCode);
                dailyCommit.setStreak(newStreak);
                dailyCommit.setLastCommitTime(commitData.lastCommitTime);
                dailyCommit.setCheckedAt(LocalDateTime.now());
            } else {
                dailyCommit = new DailyCommit(studentId, today);
                dailyCommit.setCommitsCount(commitData.commitsCount);
                dailyCommit.setDidCode(commitData.didCode);
                dailyCommit.setStreak(newStreak);
                dailyCommit.setLastCommitTime(commitData.lastCommitTime);
                dailyCommit.setCheckedAt(LocalDateTime.now());
            }

            // Update last verified timestamp
            gitHubTokenRepository.updateLastVerifiedAt(studentId, LocalDateTime.now());

            return dailyCommitRepository.save(dailyCommit);

        } catch (Exception e) {
            logger.error("Failed to fetch commits for student {}: {}", studentId, e.getMessage());
            return null;
        }
    }

    /**
     * Fetch commits for ALL connected students (used by cron job)
     */
    public Map<String, Integer> trackAllStudents() {
        List<GitHubToken> connectedStudents = gitHubTokenRepository.findByIsConnectedTrue();
        logger.info("Starting daily tracking for {} students", connectedStudents.size());

        int successCount = 0;
        int failureCount = 0;

        for (GitHubToken token : connectedStudents) {
            try {
                fetchAndSaveTodayCommits(token.getStudentId());
                successCount++;
            } catch (Exception e) {
                logger.error("Failed to track student {}: {}", token.getStudentId(), e.getMessage());
                failureCount++;
            }
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("success", successCount);
        result.put("failure", failureCount);
        result.put("total", connectedStudents.size());

        logger.info("Daily tracking complete: {} success, {} failure", successCount, failureCount);
        return result;
    }

    /**
     * Get student's current streak
     */
    public int getCurrentStreak(Long studentId) {
        return dailyCommitRepository.getCurrentStreak(studentId);
    }

    /**
     * Get student's weekly activity (last 7 days)
     */
    public List<Map<String, Object>> getWeeklyActivity(Long studentId) {
        LocalDate startDate = LocalDate.now().minusDays(7);
        List<Object[]> results = dailyCommitRepository.getWeeklyActivity(studentId, startDate);

        return results.stream().map(result -> {
            Map<String, Object> day = new HashMap<>();
            day.put("date", result[0]);
            day.put("commitsCount", result[1]);
            day.put("didCode", result[2]);
            return day;
        }).collect(Collectors.toList());
    }

    /**
     * Get total commits for a student this week
     */
    public int getTotalCommitsThisWeek(Long studentId) {
        LocalDate startOfWeek = LocalDate.now().minusDays(7);
        Integer total = dailyCommitRepository.getTotalCommitsInRange(studentId, startOfWeek, LocalDate.now());
        return total != null ? total : 0;
    }

    // ==================== AT-RISK DETECTION ====================

    /**
     * Find all students at risk (no commits for 3+ days)
     */
    public List<Map<String, Object>> getAtRiskStudents() {
        LocalDate thresholdDate = LocalDate.now().minusDays(3);
        List<Object[]> results = dailyCommitRepository.findStudentsWithNoCommitsSince(thresholdDate);

        List<Map<String, Object>> atRiskStudents = new ArrayList<>();
        for (Object[] result : results) {
            Long studentId = (Long) result[0];
            LocalDate lastCommitDate = (LocalDate) result[1];
            long daysInactive = java.time.temporal.ChronoUnit.DAYS.between(lastCommitDate, LocalDate.now());

            Map<String, Object> student = new HashMap<>();
            student.put("studentId", studentId);
            student.put("lastCommitDate", lastCommitDate);
            student.put("daysInactive", daysInactive);

            // Get GitHub username if available
            getGitHubUsername(studentId).ifPresent(username ->
                    student.put("githubUsername", username)
            );

            atRiskStudents.add(student);
        }

        return atRiskStudents;
    }

    /**
     * Get platform-wide statistics
     */
    public Map<String, Object> getPlatformStats() {
        long totalConnected = gitHubTokenRepository.count();
        long activeToday = dailyCommitRepository.findActiveStudentsByDate(LocalDate.now()).size();
        long inactiveToday = dailyCommitRepository.findInactiveStudentsByDate(LocalDate.now()).size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalConnectedStudents", totalConnected);
        stats.put("activeToday", activeToday);
        stats.put("inactiveToday", inactiveToday);
        stats.put("atRiskStudents", getAtRiskStudents().size());
        stats.put("date", LocalDate.now());

        return stats;
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private String generateState() {
        return UUID.randomUUID().toString();
    }

    private String getAccessTokenFromCode(String code) {
        String url = "https://github.com/login/oauth/access_token";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("client_id", githubClientId);
        requestBody.put("client_secret", githubClientSecret);
        requestBody.put("code", code);
        requestBody.put("redirect_uri", githubRedirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("access_token")) {
            return (String) response.getBody().get("access_token");
        }

        throw new RuntimeException("Failed to get access token from GitHub");
    }

    private Map<String, Object> getGitHubUserInfo(String accessToken) {
        String url = GITHUB_API_BASE + "/user";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);

        if (response.getBody() != null) {
            return response.getBody();
        }

        throw new RuntimeException("Failed to get user info from GitHub");
    }

    private CommitData fetchCommitsForDate(String githubUsername, String accessToken, LocalDate date) {
        String url = GITHUB_API_BASE + "/users/" + githubUsername + "/events";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, List.class);

        int totalCommits = 0;
        LocalDateTime lastCommitTime = null;

        if (response.getBody() != null) {
            String targetDateStr = date.toString();

            for (Object eventObj : response.getBody()) {
                Map<String, Object> event = (Map<String, Object>) eventObj;

                if ("PushEvent".equals(event.get("type"))) {
                    String eventDateStr = ((String) event.get("created_at")).substring(0, 10);

                    if (targetDateStr.equals(eventDateStr)) {
                        Map<String, Object> payload = (Map<String, Object>) event.get("payload");
                        List<Map<String, Object>> commits = (List<Map<String, Object>>) payload.get("commits");
                        totalCommits += commits.size();

                        if (lastCommitTime == null) {
                            lastCommitTime = LocalDateTime.parse((String) event.get("created_at"),
                                    DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        }
                    }
                }
            }
        }

        CommitData commitData = new CommitData();
        commitData.commitsCount = totalCommits;
        commitData.didCode = totalCommits > 0;
        commitData.lastCommitTime = lastCommitTime;

        return commitData;
    }

    // Inner class for commit data
    private static class CommitData {
        int commitsCount;
        boolean didCode;
        LocalDateTime lastCommitTime;
    }
}