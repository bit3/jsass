#!/usr/bin/env bash
set -ev

if [[ "linux" == $TRAVIS_OS_NAME ]]; then
  docker pull bit3/jsass-test-centos5-x32
  docker pull bit3/jsass-test-centos5-x64
  docker pull bit3/jsass-test-centos6-x32
  docker pull bit3/jsass-test-centos6-x64
  docker pull bit3/jsass-test-centos7
  docker pull bit3/jsass-test-ubuntu12.04
  docker pull bit3/jsass-test-ubuntu14.04

  mkdir -p $HOME/.gradle
  [[ "$encrypted_7aff20607abf_key" != "" && "$encrypted_7aff20607abf_iv" != "" ]] && openssl aes-256-cbc -K $encrypted_7aff20607abf_key -iv $encrypted_7aff20607abf_iv -in gradle-travis.properties.enc -out $HOME/.gradle/gradle.properties -d || true
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
