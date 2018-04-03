#!/bin/bash

set -xeuo pipefail

cd /jsass/src/main

rm -fr resources/armhf32
mkdir -p resources/armhf32

# *** Build libsass

make -C libsass clean
cd libsass
git reset --hard # hard reset
git clean -xdf # hard clean
cd ..

# We use:
# CC=arm-linux-gnueabihf-gcc
# CXX=arm-linux-gnueabihf-g++
# WINDRES=arm-linux-gnueabihf-gcc-ar
# BUILD="static"                      to make sure that we build a static library
CFLAGS="-Wall" \
CXXFLAGS="-Wall" \
LDFLAGS="-stdlib=libc++" \
CC=arm-linux-gnueabihf-gcc \
CXX=arm-linux-gnueabihf-g++ \
AR=arm-linux-gnueabihf-gcc-ar \
BUILD=static \
    make -C libsass -j4

# *** Build libjsass

rm -fr c/build
mkdir -p c/build
cd c/build
cmake -DCMAKE_TOOLCHAIN_FILE=/jsass/src/main/c/Toolchain-linux-armhf32.cmake ../
make
cp libjsass.so ../../resources/linux-armhf32/libjsass.so

# *** Build libjsass_test

cd /jsass/src/test

rm -fr resources/armhf32
mkdir -p resources/armhf32

rm -fr c/build
mkdir -p c/build
cd c/build
cmake -DCMAKE_TOOLCHAIN_FILE=/jsass/src/main/c/Toolchain-linux-armhf32.cmake ../
make
cp libjsass_test.so ../../resources/linux-armhf32/libjsass_test.so
