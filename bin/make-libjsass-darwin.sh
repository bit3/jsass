#!/usr/bin/env bash

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

cmake c
make -C c -j5
cp c/libjsass.dylib resources/darwin/libjsass.dylib
