# Documentation
Step -1
**# Clone the project**
git clone https://github.com/LifeofNabin/EnterpriseLMS.git

# Enter project folder
cd EnterpriseLMS


# First, switch to develop branch
git checkout develop

# Get latest code from GitHub
git pull origin develop

# Create your own branch
# Person 1 (Student):
git checkout -b feature/student-module

# Person 2 (Tutor):
git checkout -b feature/tutor-module

# Person 3 (Admin):
git checkout -b feature/admin-module

# Nabin (GitHub Agent):
git checkout -b feature/github-agent

Check Which Branch You're On

git branch



Switch Between Branches

git checkout main        (# Switch to main branch)
git checkout develop     (# Switch to develop branch)
git checkout feature/student-module  (# Switch to your feature branch)


💾 SAVING YOUR WORK (Daily)
Check What Changed
bash
git status
# Shows which files are modified
See Exactly What Changed
bash
git diff
# Shows the actual code changes line by line
Stage Files (Prepare to Save)
bash
# Stage a specific file
git add backend/src/main/java/com/enterpriselms/backend/student/Student.java

# Stage all files in a folder
git add backend/src/main/java/com/enterpriselms/backend/student/

# Stage ALL changed files (use carefully)
git add .
Commit (Save) Your Work
bash
# Commit with a message
git commit -m "feat(student): add Student entity and repository"

# Good commit message examples:
# ✅ "feat(student): add login API"
# ✅ "fix(student): fix registration validation"
# ✅ "style(student): format code"
# ✅ "docs(student): update README"
Push to GitHub
bash
# First time pushing this branch
git push -u origin feature/student-module

# After first time, just use:
git push
📥 GETTING OTHERS' WORK
Get Latest Code from GitHub
bash
# Get latest from current branch
git pull

# Get latest from develop branch while on your branch
git pull origin develop
Merge Develop into Your Branch
bash
# While on your feature branch
git checkout feature/student-module

# Get latest develop
git pull origin develop

# If there are conflicts, fix them (see below)
⚠️ HANDLING CONFLICTS
If Git Says "Merge Conflict"
Step 1: See which files have conflicts

bash
git status
# Files with conflicts show as "both modified"
Step 2: Open the file in VS Code/IntelliJ

Look for <<<<<<< HEAD and >>>>>>>

Keep the correct code, delete the conflict markers

Step 3: Mark as resolved

bash
git add <filename>
Step 4: Complete the merge

bash
git commit -m "resolve merge conflicts"
git push
🗑️ UNDO MISTAKES
Undo Local Changes (Before Commit)
bash
# Undo changes to a specific file
git restore backend/src/main/java/.../Student.java

# Undo ALL local changes (careful!)
git restore .
Undo Last Commit (Keep Changes)
bash
# Undo commit but keep your code changes
git reset --soft HEAD~1
Undo Last Commit (Discard Changes)
bash
# Undo commit AND discard code changes
git reset --hard HEAD~1
Stash (Temporarily Save) Work
bash
# Save current work temporarily
git stash

# Do something else (switch branch, pull code)

# Bring back your work
git stash pop
🔄 PULL REQUEST WORKFLOW
When Your Feature is Complete
Step 1: Push your final code

bash
git add .
git commit -m "feat(student): complete student module"
git push
Step 2: Go to GitHub Website

Open: https://github.com/LifeofNabin/EnterpriseLMS

Click "Pull Requests" tab

Click "New Pull Request"

Step 3: Select Branches

Base: develop

Compare: feature/student-module (or your branch)

Step 4: Create PR

Title: "feat(student): add student module"

Description: List what you built

Click "Create Pull Request"

Step 5: Wait for Review

Team lead will review

Make changes if requested

Push updates to same branch

📋 QUICK REFERENCE CARD
What You Want	Command
Clone repo	git clone <url>
Create branch	git checkout -b feature/name
Switch branch	git checkout branch-name
Check status	git status
See changes	git diff
Stage file	git add filename
Commit	git commit -m "message"
Push	git push
Pull	git pull
See commits	git log --oneline
Undo local changes	git restore filename
Stash work	git stash
Pop stash	git stash pop
✅ DAILY WORKFLOW CHEAT SHEET
Morning (Start of Day)
bash
cd EnterpriseLMS
git checkout develop
git pull origin develop
git checkout feature/your-branch
git merge develop
Throughout Day
bash
# Work on code...
git add .
git commit -m "description"
git push
Evening (End of Day)
bash
git add .
git commit -m "end of day progress"
git push
🚨 COMMON ERRORS & SOLUTIONS
Error	Solution
fatal: not a git repository	You're in wrong folder. cd EnterpriseLMS
Changes not staged for commit	Run git add first
Please tell me who you are	Run git config --global user.name/email
Merge conflict	Fix conflicts in file, then git add and git commit
Failed to push	Run git pull first, then git push
branch is ahead of origin	Run git push
📞 REMEMBER
Commit often - Every hour is good

Pull before you push - Avoid conflicts

Write clear commit messages - What did you change?

Never commit to main or develop directly - Always use feature branches

Ask for help if stuck for 15+ minutes

🎯 YOUR BRANCH NAMES
Person	Branch Name
Person 1 (Student)	feature/student-module
Person 2 (Tutor)	feature/tutor-module
Person 3 (Admin)	feature/admin-module
Nabin (GitHub Agent)	feature/github-agent
🚀 YOUR FIRST TIME COMMANDS
bash
# 1. Clone repository
git clone https://github.com/LifeofNabin/EnterpriseLMS.git
cd EnterpriseLMS

# 2. Switch to develop
git checkout develop

# 3. Create your branch
git checkout -b feature/student-module   # Change to your role

# 4. Start coding!

# 5. Save your work
git add .
git commit -m "feat: initial setup"

# 6. Push to GitHub
git push -u origin feature/student-module








