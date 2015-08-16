#!/bin/bash

cd /jsass/src/main;

rm -r resources/linux-x86-64
mkdir -p resources/linux-x86-64

# *** Build libsass

make -C libsass clean

# We use:
# - BUILD="static" to make sure that we build a static library
# - CXX=g++-4.6    to make sure that the library is linked against a version of
#                  libstdc++.so.6 that is old enough to be widely compatible
# - CC=gcc-4.4     same as for CXX but for libc.so.6
BUILD="static" make -C libsass -j8

# *** Build libjsass

rm -r c/build
mkdir -p c/build
cd c/build
cmake ../
make
cp libjsass.so ../../resources/linux-x86-64/libjsass.so
