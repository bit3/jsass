#!/bin/bash

cd /jsass/src/main

rm -r resources/linux-x64
mkdir -p resources/linux-x64

# *** Build libsass

make -C libsass clean
cd libsass
git reset --hard # hard reset
git clean -xdf # hard clean
cd ..

# We use:
# - BUILD="static" to make sure that we build a static library
BUILD="static" make -C libsass -j8 || exit 1

# *** Build libjsass

rm -r c/build
mkdir -p c/build
cd c/build
cmake ../ || exit 1
make || exit 1
cp libjsass.so ../../resources/linux-x64/libjsass.so || exit 1

# *** Build libjsass_test

cd /jsass/src/test

rm -r resources/linux-x64
mkdir -p resources/linux-x64

rm -r c/build
mkdir -p c/build
cd c/build
cmake ../ || exit 1
make || exit 1
cp libjsass_test.so ../../resources/linux-x64/libjsass_test.so || exit 1
