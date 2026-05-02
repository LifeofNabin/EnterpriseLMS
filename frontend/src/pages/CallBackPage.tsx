// src/pages/CallbackPage.tsx

import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function CallbackPage() {
  const navigate = useNavigate();
  const [message, setMessage] = useState('Connecting your GitHub account...');

  useEffect(() => {
    // Get the URL parameters
    const params = new URLSearchParams(window.location.search);
    const success = params.get('github') === 'connected';
    const error = params.get('error');

    if (success) {
      setMessage('✅ GitHub connected successfully! Redirecting to dashboard...');
      setTimeout(() => {
        navigate('/dashboard');
      }, 1500);
    } else if (error) {
      setMessage(`❌ Connection failed: ${error}. Please try again.`);
      setTimeout(() => {
        navigate('/dashboard');
      }, 3000);
    } else {
      setMessage('Processing... Redirecting to dashboard.');
      setTimeout(() => {
        navigate('/dashboard');
      }, 1000);
    }
  }, [navigate]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="text-center">
        <div className="w-12 h-12 border-4 border-blue-500 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
        <p className="text-gray-600">{message}</p>
      </div>
    </div>
  );
}

export default CallbackPage;