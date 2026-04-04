# 👨‍🏫 PERSON 2 - TUTOR MODULE

## Your Role
Build everything for tutors - course creation, student management, and grading system.

---

## 📋 WHAT TUTORS CAN DO (Features You Build)

### 1. Course Management
- Tutor can create new courses
- Tutor can add weeks to a course
- Tutor can add days to weeks
- Tutor can add tasks to days (videos, readings, assignments)
- Tutor can edit course content
- Tutor can publish/unpublish courses
- Tutor can delete courses

### 2. Student Management
- Tutor can see all students enrolled in their courses
- Tutor can view individual student progress
- Tutor can see which tasks students completed
- Tutor can view student GitHub activity
- Tutor can identify at-risk students (no commits in 7+ days)

### 3. Grading
- Tutor can see pending submissions
- Tutor can grade assignments (A, B, C, D, F)
- Tutor can write feedback for students
- Tutor can view all grades given

### 4. Analytics
- Tutor can see course completion rates
- Tutor can see average grades per course
- Tutor can see student engagement metrics

---

## 🛠️ WHAT YOU NEED TO BUILD (Backend)

### Create These REST APIs:

| API Endpoint | Purpose |
|--------------|---------|
| `POST /api/tutor/login` | Tutor login |
| `GET /api/tutor/dashboard` | Show tutor stats |
| `POST /api/tutor/courses` | Create new course |
| `GET /api/tutor/courses` | List tutor's courses |
| `GET /api/tutor/courses/{id}` | Get course details |
| `PUT /api/tutor/courses/{id}` | Update course |
| `DELETE /api/tutor/courses/{id}` | Delete course |
| `POST /api/tutor/courses/{id}/publish` | Publish course |
| `POST /api/tutor/courses/{id}/weeks` | Add week to course |
| `PUT /api/tutor/weeks/{id}` | Update week |
| `DELETE /api/tutor/weeks/{id}` | Delete week |
| `POST /api/tutor/weeks/{id}/days` | Add day to week |
| `PUT /api/tutor/days/{id}` | Update day |
| `DELETE /api/tutor/days/{id}` | Delete day |
| `POST /api/tutor/days/{id}/tasks` | Add task to day |
| `PUT /api/tutor/tasks/{id}` | Update task |
| `DELETE /api/tutor/tasks/{id}` | Delete task |
| `GET /api/tutor/courses/{id}/students` | List enrolled students |
| `GET /api/tutor/students/{id}/progress` | View student progress |
| `GET /api/tutor/submissions/pending` | List pending grades |
| `GET /api/tutor/submissions/{id}` | View submission |
| `POST /api/tutor/grade/{submissionId}` | Submit grade + feedback |
| `GET /api/tutor/analytics/courses` | Course analytics |
| `GET /api/tutor/at-risk-students` | List at-risk students |

---

## 🎨 WHAT YOU NEED TO BUILD (Frontend)

### Create These Pages/Screens:

| Page | What it Shows |
|------|---------------|
| **Login Page** | Tutor email + password |
| **Dashboard** | Stats: courses, students, pending grades |
| **My Courses** | List of all courses with edit/delete buttons |
| **Course Builder** | Form to create course + add weeks/days/tasks |
| **Course Editor** | Edit existing course content |
| **Student Roster** | List of students in a course with progress |
| **Student Progress** | Detailed view of one student's work |
| **Grading Queue** | List of pending submissions to grade |
| **Grade Assignment** | View submission, enter grade, write feedback |
| **Analytics** | Charts: completion rates, average grades |
| **At-Risk Students** | List of students with low activity |
| **Profile** | Update tutor profile |

---

## 📁 WHAT YOUR COURSE STRUCTURE LOOKS LIKE
