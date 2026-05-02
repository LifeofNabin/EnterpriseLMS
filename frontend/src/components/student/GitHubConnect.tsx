// src/components/student/GitHubConnect.tsx - NO ICONS VERSION

import { useState, useEffect } from 'react';
import { githubAPI } from '../../services/api';

interface GitHubConnectProps {
  onConnectionChange?: (isConnected: boolean) => void;
}

function GitHubConnect({ onConnectionChange }: GitHubConnectProps) {
  const [isConnected, setIsConnected] = useState(false);
  const [githubUsername, setGithubUsername] = useState<string>('');
  const [loading, setLoading] = useState(true);
  const [connecting, setConnecting] = useState(false);

  useEffect(() => {
    checkConnectionStatus();
  }, []);

  async function checkConnectionStatus() {
    try {
      setLoading(true);
      const status = await githubAPI.getStatus();
      setIsConnected(status.connected);
      setGithubUsername(status.githubUsername || '');
      onConnectionChange?.(status.connected);
    } catch (error) {
      console.error('Failed to check connection:', error);
    } finally {
      setLoading(false);
    }
  }

  async function handleConnect() {
    try {
      setConnecting(true);
      const { url } = await githubAPI.getAuthUrl();
      window.location.href = url;
    } catch (error) {
      console.error('Failed to get auth URL:', error);
      alert('Failed to connect to GitHub. Please try again.');
    } finally {
      setConnecting(false);
    }
  }

  async function handleDisconnect() {
    if (!confirm('Are you sure you want to disconnect GitHub?')) {
      return;
    }

    try {
      await githubAPI.disconnect();
      setIsConnected(false);
      setGithubUsername('');
      onConnectionChange?.(false);
      alert('GitHub account disconnected successfully.');
    } catch (error) {
      console.error('Failed to disconnect:', error);
      alert('Failed to disconnect. Please try again.');
    }
  }

  if (loading) {
    return (
      <div className="border rounded-lg p-4 bg-white shadow-sm">
        <div className="animate-pulse flex items-center space-x-3">
          <div className="w-10 h-10 bg-gray-200 rounded-full"></div>
          <div className="h-4 bg-gray-200 rounded w-32"></div>
        </div>
      </div>
    );
  }

  if (isConnected) {
    return (
      <div className="border rounded-lg p-4 bg-white shadow-sm">
        <div className="flex items-center justify-between flex-wrap gap-3">
          <div className="flex items-center gap-3">
            <span className="text-2xl">✅</span>
            <div>
              <p className="font-semibold text-green-700">GitHub Connected</p>
              <p className="text-sm text-gray-600">Connected as <span className="font-mono">{githubUsername}</span></p>
            </div>
          </div>
          <button
            onClick={handleDisconnect}
            className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-red-600 bg-red-50 rounded-lg hover:bg-red-100 transition"
          >
            🔌 Disconnect
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="border rounded-lg p-4 bg-white shadow-sm">
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <span className="text-2xl">🐙</span>
          <div>
            <p className="font-semibold text-gray-700">GitHub Not Connected</p>
            <p className="text-sm text-gray-500">Connect to track your coding activity</p>
          </div>
        </div>
        <button
          onClick={handleConnect}
          disabled={connecting}
          className="flex items-center gap-2 px-5 py-2.5 text-sm font-medium text-white bg-gray-900 rounded-lg hover:bg-black transition disabled:opacity-50"
        >
          {connecting ? (
            <>
              <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
              Redirecting to GitHub...
            </>
          ) : (
            <>
              <span>🔗</span>
              Connect GitHub Account
            </>
          )}
        </button>
      </div>
    </div>
  );
}

export default GitHubConnect;