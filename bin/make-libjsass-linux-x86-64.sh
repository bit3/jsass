#!/usr/bin/env bash

if [[ -z "$(which docker)" ]]; then
    echo "The linux build require docker"
    exit 1
fi

DIR=$(dirname $(dirname $(realpath "$0")))

# Install docker container
cd "$DIR/src/main/docker/build"
docker build -t jsass/build:latest .

# Compile binaries
docker run --user=$UID:$GID --rm -it -v "$DIR:/jsass" jsass/build:latest
