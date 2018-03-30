#!/usr/bin/env bash
set -ev

# if [[ "linux" == $TRAVIS_OS_NAME ]]; then
#   wget https://www.startssl.com/certs/ca.crt
#   sudo keytool -keystore /usr/lib/jvm/java-8-oracle-amd64/jre/lib/security/cacerts -storepass changeit -import -trustcacerts -v -noprompt -alias cacertclass1 -file ca.crt
#   ./gradlew clean assemble
#
#   if [[ $TRAVIS_PULL_REQUEST == false && $TRAVIS_REPO_SLUG == bit3/jsass && $TRAVIS_BRANCH == develop ]]; then
#     ./gradlew --stacktrace sonarqube -x test -Dsonar.login=$sonar_login -Dsonar.password=$sonar_password
#   fi
#
#   if [[ $TRAVIS_PULL_REQUEST != false ]]; then
#     ./gradlew sonarqube -x test -Dsonar.analysis.mode=issues -Dsonar.github.oauth=$sonar_github_token -Dsonar.github.pullRequest=$TRAVIS_PULL_REQUEST -Dsonar.github.repository=$TRAVIS_REPO_SLUG -Dsonar.login=$sonar_login -Dsonar.password=$sonar_password
#   fi
# fi
