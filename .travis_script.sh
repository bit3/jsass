#!/usr/bin/env bash
set -ev

if [[ "linux" == $TRAVIS_OS_NAME ]]; then
  ./gradlew runDockerTests
else
  ./bin/run-ci-tests.sh
fi
