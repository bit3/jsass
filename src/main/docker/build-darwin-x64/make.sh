#!/bin/bash

set -xeuo pipefail

cd /jsass/src/main

rm -r resources/darwin
mkdir -p resources/darwin

# *** Build libsass

make -C libsass clean
cd libsass
git reset --hard # hard reset
git clean -xdf # hard clean
cd ..

# We use:
# CC=x86_64-apple-darwin12-clang            cross compile with osxcross C compiler
# CXX=x86_64-apple-darwin12-clang++-libc++  cross compile with osxcross C++ compiler
# WINDRES=x86_64-apple-darwin12-ar          cross compile with osxcross
# BUILD="static"                            to make sure that we build a static library
CFLAGS="-Wall -arch x86_64 -stdlib=libc++" \
CXXFLAGS="-Wall -arch x86_64 -stdlib=libc++" \
LDFLAGS="-stdlib=libc++" \
CC=/opt/osxcross/target/bin/x86_64-apple-darwin12-clang \
CXX=/opt/osxcross/target/bin/x86_64-apple-darwin12-clang++-libc++ \
AR=/opt/osxcross/target/bin/x86_64-apple-darwin12-ar \
BUILD=static \
    make -C libsass -j4

# *** Build libjsass

rm -r c/build
mkdir -p c/build
cd c/build
cmake -DCMAKE_TOOLCHAIN_FILE=/jsass/src/main/c/Toolchain-darwin-x64.cmake ../
make
cp libjsass.dylib ../../resources/darwin/libjsass.dylib

# *** Build libjsass_test

cd /jsass/src/test

rm -r resources/darwin
mkdir -p resources/darwin

rm -r c/build
mkdir -p c/build
cd c/build
cmake -DCMAKE_TOOLCHAIN_FILE=/jsass/src/main/c/Toolchain-darwin-x64.cmake ../
make
cp libjsass_test.dylib ../../resources/darwin/libjsass_test.dylib
