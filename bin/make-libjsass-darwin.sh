#!/usr/bin/env bash

set -euo pipefail

cd `dirname $0`;
cd ..;

JSASS_BASE_DIR=`pwd`;

cd src/main;

rm -r resources/darwin
mkdir -p resources/darwin

# *** Build libsass

make -C libsass clean
cd libsass
git reset --hard # hard reset
git clean -xdf # hard clean
cd ..

# We use:
# - BUILD="static" to make sure that we build a static library
BUILD="static" make -C libsass -j5

# *** Build libjsass

rm -r c/build
mkdir -p c/build
cd c/build
cmake ../ || exit 1
make -j5 || exit 1
cp libjsass.dylib ../../resources/darwin/libjsass.dylib

# *** Build libjsass_test

cd $JSASS_BASE_DIR;
cd src/test;

rm -r resources/darwin
mkdir -p resources/darwin

rm -r c/build
mkdir -p c/build
cd c/build
cmake ../ || exit 1
make -j5 || exit 1
cp libjsass_test.dylib ../../resources/darwin/libjsass_test.dylib
