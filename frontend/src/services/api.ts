// src/services/api.ts

// Base URL for your Java backend
const API_BASE_URL = 'http://localhost:8080/api';

// Default student ID for testing (John Doe from your database)
// TODO: Replace with actual logged-in student ID after implementing login
const DEFAULT_STUDENT_ID = 1;

// Helper function to handle API responses
async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    // Try to get error message from response
    let errorMessage = `HTTP error! status: ${response.status}`;
    try {
      const errorData = await response.json();
      errorMessage = errorData.message || errorMessage;
    } catch {
      // If response is not JSON, use status text
      errorMessage = response.statusText || errorMessage;
    }
    throw new Error(errorMessage);
  }
  
  // If 204 No Content, return empty object
  if (response.status === 204) {
    return {} as T;
  }
  
  return response.json();
}

// ==================== TYPE DEFINITIONS ====================

export interface ConnectionStatus {
  connected: boolean;
  studentId: number;
  githubUsername?: string;
}

export interface GitHubProfile {
  studentId: number;
  isConnected: boolean;
  githubUsername?: string;
  currentStreak: number;
  totalCommitsThisWeek: number;
  weeklyActivity: WeeklyActivity[];
  generatedAt: string;
}

export interface WeeklyActivity {
  date: string;
  commitsCount: number;
  didCode: boolean;
}

export interface WeeklyActivityResponse {
  studentId: number;
  weeklyActivity: WeeklyActivity[];
  totalCommitsThisWeek: number;
  currentStreak: number;
}

export interface AtRiskStudent {
  studentId: number;
  lastCommitDate: string;
  daysInactive: number;
  githubUsername?: string;
}

export interface AtRiskResponse {
  count: number;
  students: AtRiskStudent[];
  generatedAt: string;
}

export interface PlatformStats {
  totalConnectedStudents: number;
  activeToday: number;
  inactiveToday: number;
  atRiskStudents: number;
  date: string;
}

export interface TrackResult {
  success: boolean;
  studentId: number;
  commitsCount?: number;
  didCode?: boolean;
  streak?: number;
  message?: string;
}

export interface TrackAllResult {
  success: boolean;
  totalStudents: number;
  successCount: number;
  failureCount: number;
  timestamp: string;
}

// ==================== API FUNCTIONS ====================

export const githubAPI = {
  /**
   * Get GitHub connection status for the student
   * GET /api/github/status/{studentId}
   */
  getStatus: async (): Promise<ConnectionStatus> => {
    const response = await fetch(`${API_BASE_URL}/github/status/${DEFAULT_STUDENT_ID}`);
    return handleResponse<ConnectionStatus>(response);
  },

  /**
   * Get GitHub OAuth URL for connecting account
   * GET /api/github/auth-url?studentId={id}
   */
  getAuthUrl: async (): Promise<{ url: string }> => {
    const response = await fetch(`${API_BASE_URL}/github/auth-url?studentId=${DEFAULT_STUDENT_ID}`);
    return handleResponse<{ url: string }>(response);
  },

  /**
   * Disconnect GitHub account
   * DELETE /api/github/disconnect/{studentId}
   */
  disconnect: async (): Promise<{ success: boolean; message: string }> => {
    const response = await fetch(`${API_BASE_URL}/github/disconnect/${DEFAULT_STUDENT_ID}`, {
      method: 'DELETE',
    });
    return handleResponse<{ success: boolean; message: string }>(response);
  },

  /**
   * Get complete student profile (streak, activity, etc.)
   * GET /api/github/profile/{studentId}
   */
  getProfile: async (): Promise<GitHubProfile> => {
    const response = await fetch(`${API_BASE_URL}/github/profile/${DEFAULT_STUDENT_ID}`);
    return handleResponse<GitHubProfile>(response);
  },

  /**
   * Get weekly activity (last 7 days)
   * GET /api/github/activity/weekly/{studentId}
   */
  getWeeklyActivity: async (): Promise<WeeklyActivityResponse> => {
    const response = await fetch(`${API_BASE_URL}/github/activity/weekly/${DEFAULT_STUDENT_ID}`);
    return handleResponse<WeeklyActivityResponse>(response);
  },

  /**
   * Get current streak
   * GET /api/github/streak/{studentId}
   */
  getStreak: async (): Promise<{ studentId: number; streak: number; lastUpdated: string }> => {
    const response = await fetch(`${API_BASE_URL}/github/streak/${DEFAULT_STUDENT_ID}`);
    return handleResponse<{ studentId: number; streak: number; lastUpdated: string }>(response);
  },

  /**
   * Get all at-risk students (3+ days no commits) - For Tutors/Admins
   * GET /api/github/at-risk
   */
  getAtRiskStudents: async (): Promise<AtRiskResponse> => {
    const response = await fetch(`${API_BASE_URL}/github/at-risk`);
    return handleResponse<AtRiskResponse>(response);
  },

  /**
   * Get platform-wide statistics - For Admins
   * GET /api/github/stats/platform
   */
  getPlatformStats: async (): Promise<PlatformStats> => {
    const response = await fetch(`${API_BASE_URL}/github/stats/platform`);
    return handleResponse<PlatformStats>(response);
  },

  /**
   * Manually trigger tracking for a specific student (for testing)
   * POST /api/github/track/{studentId}
   */
  trackStudent: async (studentId?: number): Promise<TrackResult> => {
    const id = studentId || DEFAULT_STUDENT_ID;
    const response = await fetch(`${API_BASE_URL}/github/track/${id}`, {
      method: 'POST',
    });
    return handleResponse<TrackResult>(response);
  },

  /**
   * Trigger tracking for ALL students (called by cron or manually)
   * POST /api/github/track-all
   */
  trackAllStudents: async (): Promise<TrackAllResult> => {
    const response = await fetch(`${API_BASE_URL}/github/track-all`, {
      method: 'POST',
    });
    return handleResponse<TrackAllResult>(response);
  },
};

// For components that need direct access to the student ID
export { DEFAULT_STUDENT_ID };