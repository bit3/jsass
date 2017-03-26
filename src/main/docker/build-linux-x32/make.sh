#!/bin/bash

set -xeuo pipefail

cd /jsass/src/main

rm -r resources/linux-x32
mkdir -p resources/linux-x32

# *** Build libsass

make -C libsass clean
cd libsass
git reset --hard # hard reset
git clean -xdf # hard clean
cd ..

# We use:
# - BUILD="static" to make sure that we build a static library
BUILD="static" make -C libsass -j$(nproc)

# *** Build libjsass

rm -r c/build
mkdir -p c/build
cd c/build
cmake -DCMAKE_TOOLCHAIN_FILE=/jsass/src/main/c/Toolchain-linux-x32.cmake ../
make
cp libjsass.so ../../resources/linux-x32/libjsass.so

# *** Build libjsass_test

cd /jsass/src/test

rm -r resources/linux-x32
mkdir -p resources/linux-x32

rm -r c/build
mkdir -p c/build
cd c/build
cmake -DCMAKE_TOOLCHAIN_FILE=/jsass/src/main/c/Toolchain-linux-x32.cmake ../
make
cp libjsass_test.so ../../resources/linux-x32/libjsass_test.so
