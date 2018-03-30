#!/bin/bash

set -euo pipefail

if [[ -d .gradle ]]; then
  rm -rf .gradle
fi

./gradlew --no-daemon --info --full-stacktrace -g /tmp clean test
