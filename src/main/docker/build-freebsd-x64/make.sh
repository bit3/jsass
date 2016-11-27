#!/bin/bash

set -xeuo pipefail

cd src/main

rm -rf resources/freebsd-x64
mkdir -p resources/freebsd-x64

# *** Build libsass

gmake -C libsass clean
cd libsass
git reset --hard # hard reset
git clean -xdf # hard clean
cd ..

# We use:
# - BUILD="static" to gmake sure that we build a static library
BUILD="static" gmake -C libsass -j$(sysctl hw.ncpu | cut -d' ' -f2)

# *** Build libjsass

rm -rf c/build
mkdir -p c/build
cd c/build
cmake ../
gmake
cp libjsass.so ../../resources/freebsd-x64/libjsass.so

# *** Build libjsass_test

cd ../../../../src/test

rm -rf resources/freebsd-x64
mkdir -p resources/freebsd-x64

rm -rf c/build
mkdir -p c/build
cd c/build
cmake ../
gmake
cp libjsass_test.so ../../resources/freebsd-x64/libjsass_test.so
