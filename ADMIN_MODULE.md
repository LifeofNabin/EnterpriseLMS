# 👑 PERSON 3 - ADMIN MODULE

## Your Role
Build everything for admins - user management, bulk import, pending approvals, and platform analytics.

---

## 📋 WHAT ADMINS CAN DO (Features You Build)

### 1. User Management
- Admin can view all users (students, tutors, admins)
- Admin can create new users
- Admin can edit user details
- Admin can delete users
- Admin can activate/deactivate users
- Admin can assign roles (student/tutor/admin)

### 2. Pending Approvals
- Admin can see list of students waiting for approval
- Admin can approve student registration
- Admin can reject student with reason
- Admin can resend approval email

### 3. Bulk Import
- Admin can download Excel template
- Admin can upload Excel file with student data
- System processes 500+ students at once
- Admin sees success/failure report
- Admin can download generated credentials

### 4. Course Oversight
- Admin can view all courses (all tutors)
- Admin can unpublish inappropriate courses
- Admin can delete any course if needed

### 5. Platform Analytics
- Admin sees total users, courses, enrollments
- Admin sees daily/weekly/monthly growth
- Admin sees popular courses
- Admin sees completion rates
- Admin sees revenue reports

### 6. System Settings
- Admin can configure site name, logo
- Admin can configure email settings
- Admin can set max students per course
- Admin can view audit logs (who did what)

---

## 🛠️ WHAT YOU NEED TO BUILD (Backend)

### Create These REST APIs:

| API Endpoint | Purpose |
|--------------|---------|
| `POST /api/admin/login` | Admin login |
| `GET /api/admin/dashboard` | Platform stats |
| `GET /api/admin/users` | List all users |
| `GET /api/admin/users/{id}` | Get user details |
| `POST /api/admin/users` | Create user |
| `PUT /api/admin/users/{id}` | Update user |
| `DELETE /api/admin/users/{id}` | Delete user |
| `PUT /api/admin/users/{id}/activate` | Activate user |
| `PUT /api/admin/users/{id}/deactivate` | Deactivate user |
| `GET /api/admin/pending-approvals` | List pending students |
| `POST /api/admin/approve/{id}` | Approve student |
| `POST /api/admin/reject/{id}` | Reject student |
| `GET /api/admin/bulk-import/template` | Download Excel template |
| `POST /api/admin/bulk-import` | Upload Excel file |
| `GET /api/admin/bulk-import/{id}/results` | Get import results |
| `GET /api/admin/courses` | List all courses |
| `DELETE /api/admin/courses/{id}` | Delete course |
| `GET /api/admin/analytics/users` | User analytics |
| `GET /api/admin/analytics/courses` | Course analytics |
| `GET /api/admin/analytics/revenue` | Revenue reports |
| `GET /api/admin/settings` | Get system settings |
| `PUT /api/admin/settings` | Update settings |
| `GET /api/admin/audit-logs` | View audit logs |

---

## 🎨 WHAT YOU NEED TO BUILD (Frontend)

### Create These Pages/Screens:

| Page | What it Shows |
|------|---------------|
| **Login Page** | Admin email + password |
| **Dashboard** | Platform stats cards + charts |
| **User Management** | Table of all users with edit/delete |
| **Add/Edit User** | Form to create or edit user |
| **Pending Approvals** | List of students waiting for approval |
| **Bulk Import** | Template download + file upload + results |
| **Course Management** | List all courses with delete option |
| **Analytics** | Charts: user growth, popular courses |
| **Settings** | System configuration form |
| **Audit Logs** | Table of all admin actions |

---

## 📊 WHAT YOUR BULK IMPORT EXCEL LOOKS LIKE

### Template Columns:
| Column | Example |
|--------|---------|
| Full Name | John Doe |
| Email | john@example.com |
| Phone | 9876543210 |
| Batch | 2024-Batch-A |
| Institute | ABC College |

### What Happens on Import:
1. System reads Excel row by row
2. Validates email format, phone number
3. Creates user account with random password
4. Sets status = ACTIVE
5. Generates credentials CSV for download

---

## 🔗 HOW YOU WORK WITH OTHERS

### You Need from Person 1 (Student):
- Student list (for user management)
- Student status data

### You Need from Person 2 (Tutor):
- Course list (for course oversight)
- Course analytics data

### Others Need from You:
- Person 1 needs: Account approval status
- Person 2 needs: User permissions
- Nabin needs: User data for GitHub tracking

---

## ✅ CHECKLIST - WHAT "DONE" LOOKS LIKE

- [ ] Admin can login
- [ ] Admin can view all users
- [ ] Admin can create/edit/delete users
- [ ] Admin can approve/reject student registrations
- [ ] Admin can download bulk import template
- [ ] Admin can upload Excel file
- [ ] Admin can import 500+ students successfully
- [ ] Admin can download credentials CSV
- [ ] Admin can view all courses
- [ ] Admin can delete inappropriate courses
- [ ] Admin can view analytics dashboard
- [ ] Admin can update system settings
- [ ] Admin can view audit logs
- [ ] All APIs work with proper error messages

---

## 📅 YOUR TIMELINE (2 Months)

| Week | Focus |
|------|-------|
| Week 1 | Login, User CRUD basics |
| Week 2 | User management UI complete |
| Week 3 | Pending approvals workflow |
| Week 4 | Bulk import - template + upload |
| Week 5 | Course oversight, delete courses |
| Week 6 | Analytics dashboard, charts |
| Week 7 | Settings, audit logs |
| Week 8 | Testing, bug fixes, polish |

---

## 📞 Who to Ask for Help

| Problem | Ask |
|---------|-----|
| User data not showing | Person 1 (Student) or Person 2 (Tutor) |
| Course data issues | Person 2 (Tutor module) |
| Server performance | Team lead |

---

## 🚀 Your First Step

1. Create `Admin.java` entity
2. Create `AdminRepository.java`
3. Create `AdminController.java` with GET /users
4. Test listing users using Postman

**That's your first day's work!**
