#!/usr/bin/env bash
set -ev

if [[ "windows" == $TRAVIS_OS_NAME ]]; then
    wget -nv --no-cookies --no-check-certificate \
         "https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_windows-x64_bin.zip" \
         -O openjdk-11.0.2_windows-x64_bin.zip
    unzip -x "openjdk-11.0.2_windows-x64_bin.zip"
    export JAVA_HOME="$(pwd)/jdk-11.0.2"
    export PATH="$JAVA_HOME/bin:$PATH"
    echo "Java Version"
    java -version
    javac -version
fi

if [[ "osx" == $TRAVIS_OS_NAME ]]; then
    wget -nv --no-cookies --no-check-certificate \
         "https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_osx-x64_bin.tar.gz" \
         -O openjdk-11.0.2_osx-x64_bin.tar.gz
    tar xf "openjdk-11.0.2_osx-x64_bin.tar.gz"
    sudo mv jdk-11.0.2.jdk /Library/Java/JavaVirtualMachines/
    echo "Java Version"
    java -version
    javac -version
fi

git fetch --unshallow

./bin/run-ci-tests.sh
