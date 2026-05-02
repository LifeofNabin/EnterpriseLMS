// src/components/student/StreakCard.tsx

interface StreakCardProps {
  streak: number;
  loading?: boolean;
}

function StreakCard({ streak, loading = false }: StreakCardProps) {
  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow-sm p-6">
        <div className="animate-pulse">
          <div className="h-4 bg-gray-200 rounded w-24 mb-4"></div>
          <div className="h-8 bg-gray-200 rounded w-16"></div>
        </div>
      </div>
    );
  }

  // Get emoji based on streak length
  const getStreakEmoji = () => {
    if (streak === 0) return '🌱';
    if (streak < 3) return '📈';
    if (streak < 7) return '🔥';
    if (streak < 14) return '⚡';
    return '🏆';
  };

  const getStreakColor = () => {
    if (streak === 0) return 'text-gray-500';
    if (streak < 3) return 'text-blue-500';
    if (streak < 7) return 'text-orange-500';
    return 'text-red-500';
  };

  return (
    <div className="bg-white rounded-lg shadow-sm p-6 text-center">
      <div className="text-5xl mb-3">{getStreakEmoji()}</div>
      <p className="text-gray-500 text-sm mb-1">Current Streak</p>
      <p className={`text-4xl font-bold ${getStreakColor()}`}>
        {streak}
      </p>
      <p className="text-gray-400 text-xs mt-2">
        {streak === 0 ? 'Make a commit to start your streak!' : 'days in a row'}
      </p>
    </div>
  );
}

export default StreakCard;