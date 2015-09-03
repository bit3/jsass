#!/bin/bash

cd /jsass/src/main;

rm -r resources/windows-x32
mkdir -p resources/windows-x32

# *** Build libsass

make -C libsass clean
cd libsass
git clean -xdf # hard reset
cd ..

# MAKE=mingw32                      to make sure we build for windows
# CC=i686-w64-mingw32-gcc           cross compile with mingw32-gcc compiler
# CXX=i686-w64-mingw32-g++          cross compile with mingw32-g++ compiler
# WINDRES=i686-w64-mingw32-windres  cross compile with mingw32-windres
# BUILD="static"                    to make sure that we build a static library
MAKE=mingw32 \
CC=i686-w64-mingw32-gcc \
CXX=i686-w64-mingw32-g++ \
WINDRES=i686-w64-mingw32-windres \
BUILD=static \
    make -C libsass -j8 lib/libsass.a

# *** Build libjsass

rm -r c/build
mkdir -p c/build
cd c/build
cmake -DCMAKE_TOOLCHAIN_FILE=../Toolchain-mingw64-x32.cmake ../
make
cp libjsass.dll ../../resources/windows-x32/libjsass.dll
