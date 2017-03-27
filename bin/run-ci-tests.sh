#!/bin/bash

set -euo pipefail

if [[ -d .gradle ]]; then
  rm -rf .gradle
fi

./gradlew --no-daemon -g /tmp clean test
