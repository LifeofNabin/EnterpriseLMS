# 👨‍🎓 PERSON 1 - STUDENT MODULE

## Your Responsibility
Build the complete Student Portal - both backend and frontend.

---

## 📋 WHAT STUDENTS CAN DO (Features You Build)

### 1. Account Management
- Student can register (create account)
- Student can login
- Student can reset forgotten password
- Student can update profile (name, phone, password)
- Student can view their own dashboard

### 2. Course Access
- Student can see all available courses
- Student can enroll in a course
- Student can see their enrolled courses
- Student can view course content (videos, readings, assignments)
- Student can track their progress (what % completed)

### 3. Assignments & Submissions
- Student can view tasks/assignments
- Student can submit text answers
- Student can upload files for assignments
- Student can submit GitHub repository link
- Student can see submission history

### 4. Grades & Feedback
- Student can view their grades
- Student can read trainer's feedback
- Student can see overall course grade

### 5. Certificates
- Student can view earned certificates
- Student can download certificate as PDF

### 6. GitHub Integration (Your part connects here)
- Student can connect their GitHub account
- Student can see their commit activity heatmap
- Student can view their commit history

---

## 🛠️ WHAT YOU NEED TO BUILD (Backend)

### Create These REST APIs:

| API Endpoint | Purpose |
|--------------|---------|
| `POST /api/student/register` | Create new student account |
| `POST /api/student/login` | Student login |
| `POST /api/student/forgot-password` | Reset password |
| `GET /api/student/profile` | Get student info |
| `PUT /api/student/profile` | Update profile |
| `GET /api/student/courses` | List enrolled courses |
| `GET /api/student/courses/available` | List all available courses |
| `POST /api/student/enroll/{courseId}` | Enroll in course |
| `GET /api/student/courses/{id}/content` | View course content |
| `GET /api/student/progress/{courseId}` | Track progress |
| `GET /api/student/tasks` | List pending tasks |
| `POST /api/student/tasks/{id}/submit` | Submit assignment |
| `GET /api/student/grades` | View grades |
| `GET /api/student/certificates` | List certificates |
| `GET /api/student/certificates/{id}/download` | Download PDF |
| `POST /api/student/github/connect` | Connect GitHub |
| `GET /api/student/github/activity` | View commit activity |

---

## 🎨 WHAT YOU NEED TO BUILD (Frontend)

### Create These Pages/Screens:

| Page | What it Shows |
|------|---------------|
| **Login Page** | Email + password form |
| **Register Page** | Name, email, password, phone form |
| **Forgot Password Page** | Email input + reset link |
| **Dashboard** | Progress cards, enrolled courses, pending tasks |
| **Available Courses** | List of courses student can enroll in |
| **My Courses** | List of enrolled courses with progress |
| **Course Content** | Week by week, day by day, task by task |
| **Task Submission** | Form to submit text/file/GitHub link |
| **Grades Page** | List of all grades with feedback |
| **Certificates Page** | List of earned certificates with download |
| **Profile Page** | Edit profile, change password |
| **GitHub Connect** | Button to connect GitHub account |
| **Activity Heatmap** | Calendar showing commit activity |

---

## 🔗 HOW YOU WORK WITH OTHERS

### You Need from Person 2 (Tutor):
- Course data (title, description, weeks, days, tasks)
- Course content (videos, readings, assignments)
- Grades after trainer marks them

### You Need from Person 3 (Admin):
- Nothing directly (admin manages users separately)

### You Need from Nabin (GitHub Agent):
- GitHub OAuth connection
- Commit activity data
- Heatmap data

### Others Need from You:
- Person 2 needs: Student enrollment data, submissions
- Person 3 needs: Student list for reports
- Nabin needs: Student ID for GitHub tracking

---

## ✅ CHECKLIST - WHAT "DONE" LOOKS LIKE

- [ ] Student can register and login
- [ ] Student can see available courses
- [ ] Student can enroll in a course
- [ ] Student can view course content (videos/readings)
- [ ] Student can submit assignments
- [ ] Student can upload files
- [ ] Student can submit GitHub links
- [ ] Student can view their grades
- [ ] Student can download certificates
- [ ] Student can connect GitHub account
- [ ] Student can see commit heatmap
- [ ] All APIs work with proper error messages
- [ ] All pages work on mobile and desktop

---

## 📅 YOUR TIMELINE (2 Months)

| Week | Focus |
|------|-------|
| Week 1 | Login, Register, JWT authentication |
| Week 2 | Dashboard, Profile management |
| Week 3 | Course listing, Enrollment |
| Week 4 | Course content viewing |
| Week 5 | Task submission, File upload |
| Week 6 | Grades view, Certificates |
| Week 7 | GitHub connect, Activity heatmap |
| Week 8 | Testing, Bug fixes, Polish |

---

## 📞 Who to Ask for Help

| Problem | Ask |
|---------|-----|
| Course data not showing | Person 2 (Tutor module) |
| GitHub activity not loading | Nabin (GitHub Agent) |
| Server not starting | Team lead |
| React component styling | Anyone |

---
