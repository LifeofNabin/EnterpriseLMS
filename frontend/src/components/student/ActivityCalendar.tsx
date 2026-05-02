// src/components/student/ActivityCalendar.tsx

interface WeeklyActivity {
  date: string;
  commitsCount: number;
  didCode: boolean;
}

interface ActivityCalendarProps {
  weeklyActivity: WeeklyActivity[];
  loading?: boolean;
}

function ActivityCalendar({ weeklyActivity, loading = false }: ActivityCalendarProps) {
  // Days of week
  const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  
  // Get today's date to determine which days to show
  const getDateLabel = (dateStr: string) => {
    const today = new Date().toISOString().split('T')[0];
    const yesterday = new Date(Date.now() - 86400000).toISOString().split('T')[0];
    
    if (dateStr === today) return 'Today';
    if (dateStr === yesterday) return 'Yesterday';
    return '';
  };

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow-sm p-6">
        <div className="animate-pulse">
          <div className="h-4 bg-gray-200 rounded w-32 mb-4"></div>
          <div className="grid grid-cols-7 gap-2">
            {[...Array(7)].map((_, i) => (
              <div key={i} className="h-16 bg-gray-200 rounded"></div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (!weeklyActivity || weeklyActivity.length === 0) {
    return (
      <div className="bg-white rounded-lg shadow-sm p-6 text-center">
        <p className="text-gray-500">No activity data yet.</p>
        <p className="text-gray-400 text-sm mt-1">Connect GitHub and make commits to see your calendar!</p>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow-sm p-6">
      <h3 className="font-semibold text-gray-800 mb-4">Weekly Activity</h3>
      
      <div className="grid grid-cols-7 gap-2">
        {weeklyActivity.map((day, index) => {
          const dayName = days[index % 7];
          const isToday = getDateLabel(day.date) === 'Today';
          const isYesterday = getDateLabel(day.date) === 'Yesterday';
          
          return (
            <div key={index} className="text-center">
              <div className="text-xs text-gray-500 mb-1">{dayName}</div>
              <div
                className={`
                  h-12 rounded-lg flex items-center justify-center cursor-help
                  transition-transform hover:scale-105
                  ${day.didCode 
                    ? 'bg-green-500 text-white hover:bg-green-600' 
                    : 'bg-gray-100 text-gray-400'
                  }
                  ${isToday ? 'ring-2 ring-blue-400' : ''}
                  ${isYesterday ? 'ring-1 ring-gray-300' : ''}
                `}
                title={`${day.date}: ${day.commitsCount} commits`}
              >
                <span className="text-sm font-medium">{day.commitsCount}</span>
              </div>
              {isToday && <div className="text-xs text-blue-500 mt-1">Today</div>}
            </div>
          );
        })}
      </div>
      
      <div className="flex justify-center gap-4 mt-4 text-xs text-gray-500">
        <div className="flex items-center gap-1">
          <div className="w-3 h-3 bg-green-500 rounded"></div>
          <span>Coded</span>
        </div>
        <div className="flex items-center gap-1">
          <div className="w-3 h-3 bg-gray-100 rounded"></div>
          <span>No code</span>
        </div>
        <div className="flex items-center gap-1">
          <div className="w-3 h-3 ring-2 ring-blue-400 rounded"></div>
          <span>Today</span>
        </div>
      </div>
    </div>
  );
}

export default ActivityCalendar;