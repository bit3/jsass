#!/bin/bash

set -xeuo pipefail

cd /jsass/src/main

rm -fr resources/linux-x32
mkdir -p resources/linux-x32

# *** Build libsass

make -C libsass clean
cd libsass
git reset --hard # hard reset
git clean -xdf # hard clean
cd ..

# We use:
# - BUILD="static" to make sure that we build a static library
BUILD="static" make -C libsass -j4

# *** Build libjsass

rm -fr c/build
mkdir -p c/build
cd c/build
cmake ../
make
cp libjsass.so ../../resources/linux-x32/libjsass.so

# *** Build libjsass_test

cd /jsass/src/test

rm -fr resources/linux-x32
mkdir -p resources/linux-x32

rm -fr c/build
mkdir -p c/build
cd c/build
cmake ../
make
cp libjsass_test.so ../../resources/linux-x32/libjsass_test.so
