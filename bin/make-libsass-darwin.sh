#!/usr/bin/env bash

cd `dirname $0`
cd ../src/main/libsass

autoreconf --force --install
./configure --disable-tests --enable-shared 

make -j5

cd ..

mkdir -p resources/darwin
cp libsass/.libs/libsass.0.dylib resources/darwin/libsass.dylib

cd ..
