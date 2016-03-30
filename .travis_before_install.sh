#!/usr/bin/env bash
set -ev

if [[ "linux" == $TRAVIS_OS_NAME ]]; then
  docker pull bit3/jsass-test-centos5
  docker pull bit3/jsass-test-centos6
  docker pull bit3/jsass-test-centos7
  docker pull bit3/jsass-test-ubuntu12.04
  docker pull bit3/jsass-test-ubuntu14.04

  mkdir -p $HOME/.gradle
  [[ "$encrypted_7aff20607abf_key" != "" && "$encrypted_7aff20607abf_iv" != "" ]] && openssl aes-256-cbc -K $encrypted_7aff20607abf_key -iv $encrypted_7aff20607abf_iv -in gradle-travis.properties.enc -out $HOME/.gradle/gradle.properties -d || true
fi

if [[ "osx" == $TRAVIS_OS_NAME ]]; then
    wget --no-cookies --no-check-certificate \
         --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" \
         "http://download.oracle.com/otn-pub/java/jdk/8u77-b03/jdk-8u77-macosx-x64.dmg" \
         -O jdk-8u77-macosx-x64.dmg
    hdiutil mount jdk-8u77-macosx-x64.dmg
    mount
    sudo installer -package "/Volumes/JDK 8 Update 77/JDK 8 Update 77.pkg" -target "/"
fi

git fetch --unshallow
