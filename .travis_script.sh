#!/usr/bin/env bash
set -ev

if [[ "windows" == $TRAVIS_OS_NAME ]]; then
    wget -nv --no-cookies --no-check-certificate \
         "https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.6%2B10/OpenJDK11U-jdk_x64_windows_hotspot_11.0.6_10.zip" \
         -O OpenJDK11U-jdk_x64_windows_hotspot_11.0.6_10.zip
    unzip -x "OpenJDK11U-jdk_x64_windows_hotspot_11.0.6_10.zip"
    export JAVA_HOME="$(pwd)/jdk-11.0.6+10"
    export PATH="$JAVA_HOME/bin:$PATH"
    echo "Java Version"
    java -version
    javac -version
fi

if [[ "osx" == $TRAVIS_OS_NAME ]]; then
    wget -nv --no-cookies --no-check-certificate \
         "https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.6%2B10/OpenJDK11U-jdk_x64_mac_hotspot_11.0.6_10.tar.gz" \
         -O OpenJDK11U-jdk_x64_mac_hotspot_11.0.6_10.tar.gz
    tar xf "OpenJDK11U-jdk_x64_mac_hotspot_11.0.6_10.tar.gz"
    sudo mv jdk-11.0.6+10 /Library/Java/JavaVirtualMachines/
    export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk-11.0.6+10/Contents/Home"
    echo "Java Version"
    java -version
    javac -version
fi

git fetch --unshallow

./bin/run-ci-tests.sh
