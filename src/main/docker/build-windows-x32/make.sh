#!/bin/bash

set -xeuo pipefail

cd /jsass/src/main

rm -r resources/windows-x32
mkdir -p resources/windows-x32

# *** Build libsass

make -C libsass clean
cd libsass
git reset --hard # hard reset
git clean -xdf # hard clean
cd ..

# *** Prepare makefile to use static bindings

# static libgcc and libstdc++
sed -i 's/LDFLAGS  += -std=gnu++0x/LDFLAGS  += -std=gnu++0x -static-libgcc -static-libstdc++/' libsass/Makefile

# static windows bindings
# @see https://github.com/sass/libsass/wiki/Building-with-MinGW#building-via-minggw-64bit-makefiles
sed -i 's/ -Wl,--subsystem,windows/ -static -Wl,--subsystem,windows/' libsass/Makefile

# We use:
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
    make -C libsass -j8 lib/libsass.dll
cp libsass/lib/libsass.dll resources/windows-x32/libsass.dll

# *** Build libjsass

rm -r c/build
mkdir -p c/build
cd c/build
cmake -DCMAKE_TOOLCHAIN_FILE=../Toolchain-mingw64-x32.cmake ../
make
cp libjsass.dll ../../resources/windows-x32/libjsass.dll

# *** Build libjsass_test

cd /jsass/src/test

rm -r resources/windows-x32
mkdir -p resources/windows-x32

rm -r c/build
mkdir -p c/build
cd c/build
cmake -DCMAKE_TOOLCHAIN_FILE=../Toolchain-mingw64-x32.cmake ../
make
cp libjsass_test.dll ../../resources/windows-x32/libjsass_test.dll
