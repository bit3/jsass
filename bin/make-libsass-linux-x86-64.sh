#!/usr/bin/env bash

cd `dirname $0`;
cd ../src/main;

make -C libsass clean

# We use:
# - BUILD="shared" to make sure that we build a shared system library
# - CXX=g++-4.6    to make sure that the library is linked against a version of
#                  libstdc++.so.6 that is old enough to be widely compatible
# - CC=gcc-4.4     same as for CXX but for libc.so.6
BUILD="shared" make -C libsass -j5 CXX=g++-4.6 CC=gcc-4.4

rm -r resources/linux-x86-64
mkdir -p resources/linux-x86-64
cp libsass/lib/libsass.so resources/linux-x86-64/libsass.so
cd ..;