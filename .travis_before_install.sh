#!/usr/bin/env bash
set -ev

wget -nv --no-cookies --no-check-certificate \
     "https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_osx-x64_bin.tar.gz" \
     -O openjdk-11.0.2_osx-x64_bin.tar.gz
tar xf "openjdk-11.0.2_osx-x64_bin.tar.gz"
sudo mv jdk-11.0.2.jdk /Library/Java/JavaVirtualMachines/
echo "Java Version"
java -version
javac -version

git fetch --unshallow
