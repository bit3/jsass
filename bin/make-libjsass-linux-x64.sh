#!/usr/bin/env bash

if [[ -z "$(which docker)" ]]; then
    echo "The linux build require docker"
    exit 1
fi

if [[ -z "$(which gradle)" ]]; then
    echo "The linux build require gradle"
    exit 1
fi

DIR=$(dirname $(dirname $(realpath "$0")))
cd "$DIR"

# Install docker container
gradle buildDockerBuildLinux64

# Compile binaries
gradle buildNativeLinux64Libs
