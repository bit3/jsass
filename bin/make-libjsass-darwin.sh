#!/usr/bin/env bash

set -euo pipefail

cd `dirname $0`;
cd ../src/main;

rm -r resources/darwin
mkdir -p resources/darwin

# *** Build libsass

make -C libsass clean

# We use:
# - BUILD="static" to make sure that we build a static library
BUILD="static" make -C libsass -j5

# *** Build libjsass

rm -r c/build
mkdir -p c/build
cd c/build
cmake ../
make -j5
cp libjsass.dylib ../../resources/darwin/libjsass.dylib
