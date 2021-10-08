export DEPLOY_BRANCH="$CIRCLE_BRANCH"

curl \
  -F "token=$SLACK_TOKEN" \
  -F "channels=android" \
  -F "initial_comment=$DEPLOY_BRANCH" \
  -F "file=@app/build/outputs/apk/debug/app-debug.apk" \
  https://slack.com/api/files.upload