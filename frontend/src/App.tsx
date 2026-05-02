// src/App.tsx

import { BrowserRouter, Routes, Route } from 'react-router-dom';
import StudentDashboard from './components/student/StudentDashboard';
import CallbackPage from './pages/CallBackPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<StudentDashboard />} />
        <Route path="/dashboard" element={<StudentDashboard />} />
        <Route path="/callback" element={<CallbackPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;