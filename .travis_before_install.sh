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
         --header "Cookie: oraclelicense=accept-securebackup-cookie" \
         "http://download.oracle.com/otn-pub/java/jdk/8u161-b12/2f38c3b165be4555a1fa6e98c45e0808/jdk-8u161-macosx-x64.dmg" \
         -O jdk-8u161-macosx-x64.dmg
    hdiutil mount jdk-8u161-macosx-x64.dmg
    sudo installer -package "/Volumes/JDK 8 Update 161/JDK 8 Update 161.pkg" -target "/"
fi

git fetch --unshallow
