#!/usr/bin/env bash

set -euo pipefail

if [[ -z "$(which docker)" ]]; then
    echo "The windows build require docker"
    exit 1
fi

if [[ -z "$(which gradle)" ]]; then
    echo "The linux build require gradle"
    exit 1
fi

DIR=$(dirname $(dirname $(realpath "$0")))
cd "$DIR"

# Compile binaries
./gradlew -g /tmp buildNativeWindows64Libs
