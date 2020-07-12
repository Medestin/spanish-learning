set remote = %1
set branch = %2
set url = http://%jenkins_automation_credentials%@localhost:8081/job/spanish-learning/build?token=86aa139d-bb3f-435f-8eac-62594b34c72a

git push %1 %2
if %ERRORLEVEL%==0 (curl -X POST http://%jenkins_automation_credentials%@localhost:8081/job/spanish-learning/build?token=86aa139d-bb3f-435f-8eac-62594b34c72a)
