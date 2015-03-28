#!/usr/bin/env bash

cd `dirname $0`;
cd src/main/libsass;

autoreconf --force --install

./configure --disable-tests --enable-shared 

make -j5

cd ..;

cp libsass/.libs/libsass.0.dylib resources/darwin/libsass.dylib
cd ..;
