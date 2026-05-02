// src/components/student/StudentDashboard.tsx

import { useState, useEffect } from 'react';
import GitHubConnect from './GitHubConnect';
import StreakCard from './StreakCard';
import ActivityCalendar from './ActivityCalendar';
import { githubAPI } from '../../services/api';

interface DashboardData {
  streak: number;
  weeklyActivity: Array<{
    date: string;
    commitsCount: number;
    didCode: boolean;
  }>;
  totalCommits: number;
  isConnected: boolean;
}

function StudentDashboard() {
  const [data, setData] = useState<DashboardData | null>(null);
  const [loading, setLoading] = useState(true);
  const [isConnected, setIsConnected] = useState(false);

  // Load dashboard data
  const loadDashboardData = async () => {
    try {
      setLoading(true);
      const profile = await githubAPI.getProfile();
      
      setData({
        streak: profile.currentStreak,
        weeklyActivity: profile.weeklyActivity || [],
        totalCommits: profile.totalCommitsThisWeek,
        isConnected: profile.isConnected,
      });
      setIsConnected(profile.isConnected);
    } catch (error) {
      console.error('Failed to load dashboard:', error);
    } finally {
      setLoading(false);
    }
  };

  // Load data when component mounts
  useEffect(() => {
    loadDashboardData();
  }, []);

  // Reload data when GitHub connection changes
  const handleConnectionChange = (connected: boolean) => {
    setIsConnected(connected);
    if (connected) {
      // Small delay to allow backend to process
      setTimeout(loadDashboardData, 1000);
    }
  };

  return (
    <div className="max-w-6xl mx-auto p-6">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Student Dashboard</h1>
      
      <div className="space-y-6">
        {/* GitHub Connection Card */}
        <GitHubConnect onConnectionChange={handleConnectionChange} />
        
        {/* Only show stats if connected */}
        {isConnected ? (
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            {/* Streak Card - spans 1 column */}
            <div className="lg:col-span-1">
              <StreakCard streak={data?.streak || 0} loading={loading} />
            </div>
            
            {/* Stats Summary - spans 2 columns */}
            <div className="lg:col-span-2 bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg shadow-sm p-6">
              <div className="flex justify-between items-center">
                <div>
                  <p className="text-gray-500 text-sm">Total Commits This Week</p>
                  <p className="text-3xl font-bold text-blue-600">
                    {loading ? '...' : data?.totalCommits || 0}
                  </p>
                </div>
                <div className="text-right">
                  <p className="text-gray-500 text-sm">Best Streak</p>
                  <p className="text-3xl font-bold text-indigo-600">
                    {loading ? '...' : data?.streak || 0}
                  </p>
                </div>
              </div>
            </div>
          </div>
        ) : (
          <div className="bg-gray-50 rounded-lg p-8 text-center text-gray-500">
            <p className="text-lg">📊 Connect your GitHub account to see your coding activity!</p>
            <p className="text-sm mt-2">We'll track your commits and show your streak.</p>
          </div>
        )}
        
        {/* Activity Calendar - full width */}
        {isConnected && (
          <ActivityCalendar 
            weeklyActivity={data?.weeklyActivity || []} 
            loading={loading} 
          />
        )}
      </div>
    </div>
  );
}

export default StudentDashboard;