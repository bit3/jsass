#!/bin/bash

set -euo pipefail

if [[ -d .gradle ]]; then
  rm -rf .gradle
fi

mkdir -p .gradle_home

./gradlew --info --full-stacktrace --gradle-user-home "$PWD/.gradle_home" clean test "$@"
