#!/bin/bash

echo
echo -e "\e[91m+-------------------------------------------------------------------------+\e[0m"
echo -e "\e[91m|                                                                         |\e[0m"
echo -e "\e[91m|   !!! Attention !!! This file is intended for local development only!   |\e[0m"
echo -e "\e[91m|                                                                         |\e[0m"
echo -e "\e[91m+-------------------------------------------------------------------------+\e[0m"
echo

set -xeuo pipefail

BASE_DIR=$(dirname $(dirname $(readlink -e "$0")))

cd "$BASE_DIR/src/main"

rm -fr resources/linux-x64
mkdir -p resources/linux-x64

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
cp libjsass.so ../../resources/linux-x64/libjsass.so

# *** Build libjsass_test

cd "$BASE_DIR/src/test"

rm -fr resources/linux-x64
mkdir -p resources/linux-x64

rm -fr c/build
mkdir -p c/build
cd c/build
cmake ../
make
cp libjsass_test.so ../../resources/linux-x64/libjsass_test.so
