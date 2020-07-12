#!/bin/bash
#jenkins_automation_credentials is an environment variable
remote="$1"
branch="$2"
git push ${remote} ${branch}
if [ $? -eq 0 ]; then
  credentials="${jenkins_automation_credentials}"
  curl -X POST http://${credentials}@localhost:8081/job/ftp-client/build?token=86aa139d-bb3f-435f-8eac-62594b34c72a
fi
