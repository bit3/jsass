[![pipeline status](https://gitlab.com/jsass/jsass/badges/master/pipeline.svg)](https://gitlab.com/jsass/jsass/commits/master)
[![Build Status](https://img.shields.io/travis/bit3/jsass/master.svg?style=flat&logo=travis)](https://travis-ci.org/bit3/jsass)
[![Build Status](https://cloud.drone.io/api/badges/bit3/jsass/status.svg)](https://cloud.drone.io/bit3/jsass)
[![Scrutinizer Code Quality](https://scrutinizer-ci.com/g/bit3/jsass/badges/quality-score.png?b=master)](https://scrutinizer-ci.com/g/bit3/jsass/?branch=master)
[![Code Coverage](https://scrutinizer-ci.com/g/bit3/jsass/badges/coverage.png?b=master)](https://scrutinizer-ci.com/g/bit3/jsass/?branch=master)
[![Known Vulnerabilities](https://snyk.io/test/github/bit3/jsass/badge.svg)](https://snyk.io/test/github/bit3/jsass)
[![Documentation Status](https://readthedocs.org/projects/jsass/badge/?version=latest)](http://jsass.readthedocs.io/en/latest/)
[![Javadoc Status](https://javadocio-badges.herokuapp.com/io.bit3/jsass/badge.svg)](http://javadoc.io/doc/io.bit3/jsass/)

Java sass compiler
==================

Feature complete java sass compiler using [libsass][libsass] version 3.6.4.

The most advantage of jsass is to hide the libsass complexity from the developer and provide a more java like way.

For complete documentation, see [jsass.rtfd.org][jsass-docs].

[libsass]: https://github.com/sass/libsass
[jsass-docs]: http://jsass.rtfd.org/

Example
-------

There is a [webapp example](example/webapp) containing a servlet sample implementation, to illustrate the way you
may integrate jsass into your webapp.

Changelog
---------

You can find the changelog in our documentation at [jsass.rtfd.org/en/latest/changelog.html][changelog]

[changelog]: http://jsass.readthedocs.org/en/latest/changelog.html

Third party projects
--------------------

Third party projects using JSASS.

- [libsass-maven-plugin](https://gitlab.com/haynes/libsass-maven-plugin)
- [JSass Gradle Plugin](https://plugins.gradle.org/plugin/io.freefair.jsass-java)
- [lein-jsass (Clojure)](https://clojars.org/lein-jsass)
- [Deraen/sass4clj (Clojure)](https://github.com/Deraen/sass4clj)
- [oVirt](https://www.ovirt.org/)

Merge requests for further projects are welcome :-)

Compatibility Overview
----------------------

| Compatibility                              |                                                                      |
| -------------------------------------------|----------------------------------------------------------------------|
| ![Java 7][java7]                           | not supported, jsass uses Java 8 features like streams and lambdas!  |
| ![Java 8][java8]                           | fully supported                                                      |
| ![Java 11][java11]                         | fully supported and [tested][gitlab-ci] (gitlab ci)                  |
| **Linux**                                  |                                                                      |
| ![CentOS 6 (i686)][centos6_32]             | not supported (since jsass 5.7.4 the 32bit support was been removed) |
| ![CentOS 6 (x86_64)][centos6]              | fully supported and [tested][gitlab-ci] (gitlab ci)                  |
| ![CentOS 7 (x86_64)][centos7]              | fully supported and [tested][gitlab-ci] (gitlab ci)                  |
| ![ubuntu 16.04 (x86_64)][ubuntu16.04]      | fully supported and [tested][gitlab-ci] (gitlab ci)                  |
| ![ubuntu 18.04 (x86_64)][ubuntu18.04]      | fully supported and [tested][gitlab-ci] (gitlab ci)                  |
| ![Debian Stretch (x86_64)][debian-stretch] | fully supported and [tested][gitlab-ci] (gitlab ci)                  |
| ![Debian Buster (x86_64)][debian-buster]   | fully supported and [tested][gitlab-ci] (gitlab ci)                  |
| ![Debian Stretch (armhf32)][armhf32]       | experimental, [tests][drone-io] are failing (drone.io)               |
| ![Debian Stretch (aarch64)][aarch64]       | fully supported and [tested][drone-io] (drone.io)                    |
| **Windows**                                |                                                                      |
| ![Windows 32bit][windows32]                | not supported                                                        |
| ![Windows 64bit][windows64]                | fully supported and [tested][travis-ci] (travis ci)                  |
| **macOS**                                  |                                                                      |
| ![macOS][macos]                            | fully supported and [tested][travis-ci] (travis ci)                  |
| **Others**                                 |                                                                      |
| ![Solaris][solaris]                        | not supported                                                        |
| ![FreeBSD][freebsd]                        | not supported                                                        |

[java7]: https://img.shields.io/badge/Java-7-red.svg?style=flat
[java8]: https://img.shields.io/badge/Java-8-green.svg?style=flat
[java11]: https://img.shields.io/badge/Java-11-green.svg?style=flat

[centos6_32]: https://img.shields.io/badge/CentOS_6-x86-red.svg?style=flat
[centos6]: https://img.shields.io/badge/CentOS_6-x86__64-green.svg?style=flat
[centos7]: https://img.shields.io/badge/CentOS_7-x86__64-green.svg?style=flat

[ubuntu16.04]: https://img.shields.io/badge/ubuntu_16.04-x86__64-green.svg?style=flat
[ubuntu18.04]: https://img.shields.io/badge/ubuntu_18.04-x86__64-green.svg?style=flat

[debian-stretch]: https://img.shields.io/badge/Debian_Stretch-x86__64-green.svg?style=flat
[debian-buster]: https://img.shields.io/badge/Debian_Buster-x86__64-green.svg?style=flat

[armhf32]: https://img.shields.io/badge/Debian_Stretch-arm_(armhf32)-yellow.svg?style=flat
[aarch64]: https://img.shields.io/badge/Debian_Stretch-arm64_(aarch64)-green.svg?style=flat

[windows32]: https://img.shields.io/badge/Windows-32bit-red.svg?style=flat
[windows64]: https://img.shields.io/badge/Windows-64bit-green.svg?style=flat

[macos]: https://img.shields.io/badge/macOS-10+-green.svg?style=flat

[gitlab-ci]: https://gitlab.com/jsass/jsass/pipelines
[travis-ci]: https://travis-ci.org/bit3/jsass
[drone-io]: https://cloud.drone.io/bit3/jsass

[solaris]: https://img.shields.io/badge/Solaris-red.svg?style=flat
[freebsd]: https://img.shields.io/badge/FreeBSD-red.svg?style=flat

Testing Matrix
--------------

A matrix of all CI systems and jobs, used for testing.

|                                            | OpenJDK 8                                                                           | OpenJDK 11                                                                           | OpenJDK 13                                                                           |
|--------------------------------------------|-------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| ![CentOS 6 (x86_64)][centos6]              | ![Gitlab CI / test-centos6-openjdk8][gitlab-ci-test-centos6-openjdk8]               | ![Gitlab CI / test-centos6-openjdk8][gitlab-ci-test-centos6-openjdk11]               | ![Gitlab CI / test-centos6-openjdk8][gitlab-ci-test-centos6-openjdk13]               |
| ![CentOS 7 (x86_64)][centos7]              | ![Gitlab CI / test-centos7-openjdk8][gitlab-ci-test-centos7-openjdk8]               | ![Gitlab CI / test-centos7-openjdk8][gitlab-ci-test-centos7-openjdk11]               | ![Gitlab CI / test-centos7-openjdk8][gitlab-ci-test-centos7-openjdk13]               |
| ![ubuntu 16.04 (x86_64)][ubuntu16.04]      | ![Gitlab CI / test-ubuntu16.04-openjdk8][gitlab-ci-test-ubuntu16.04-openjdk8]       | ![Gitlab CI / test-ubuntu16.04-openjdk8][gitlab-ci-test-ubuntu16.04-openjdk11]       | ![Gitlab CI / test-ubuntu16.04-openjdk8][gitlab-ci-test-ubuntu16.04-openjdk13]       |
| ![ubuntu 18.04 (x86_64)][ubuntu18.04]      | ![Gitlab CI / test-ubuntu18.04-openjdk8][gitlab-ci-test-ubuntu18.04-openjdk8]       | ![Gitlab CI / test-ubuntu18.04-openjdk8][gitlab-ci-test-ubuntu18.04-openjdk11]       | ![Gitlab CI / test-ubuntu18.04-openjdk8][gitlab-ci-test-ubuntu18.04-openjdk13]       |
| ![Debian Stretch (x86_64)][debian-stretch] | ![Gitlab CI / test-debian-stretch-openjdk8][gitlab-ci-test-debian-stretch-openjdk8] | ![Gitlab CI / test-debian-stretch-openjdk8][gitlab-ci-test-debian-stretch-openjdk11] | ![Gitlab CI / test-debian-stretch-openjdk8][gitlab-ci-test-debian-stretch-openjdk13] |
| ![Debian Buster (x86_64)][debian-buster]   | ![Gitlab CI / test-debian-buster-openjdk8][gitlab-ci-test-debian-buster-openjdk8]   | ![Gitlab CI / test-debian-buster-openjdk8][gitlab-ci-test-debian-buster-openjdk11]   | ![Gitlab CI / test-debian-buster-openjdk8][gitlab-ci-test-debian-buster-openjdk13]   |
| ![ARM (armhf32)][armhf32]                  | ![Drone.io / test-arm-openjdk8][drone.io-test-arm-openjdk8]                         | ![Drone.io / test-arm-openjdk8][drone.io-test-arm-openjdk11]                         |                                                                                      |
| ![ubuntu 16.04 (aarch64)][aarch64]         | ![Drone.io / test-arm64-openjdk8][drone.io-test-arm64-openjdk8]                     | ![Drone.io / test-arm64-openjdk8][drone.io-test-arm64-openjdk11]                     |                                                                                      |
| ![Windows 64bit][windows64]                |                                                                                     | ![Travis CI / OS: Windows][travis-ci-os-windows]                                     |                                                                                      |
| ![macOS][macos]                            |                                                                                     | ![Travis CI / OS: macOS][travis-ci-os-macos]                                         |                                                                                      |

[gitlab-ci-test-centos6-openjdk8]: https://img.shields.io/badge/Gitlab_CI-test--centos6--openjdk8-blue.svg?style=flat
[gitlab-ci-test-centos6-openjdk11]: https://img.shields.io/badge/Gitlab_CI-test--centos6--openjdk11-blue.svg?style=flat
[gitlab-ci-test-centos6-openjdk13]: https://img.shields.io/badge/Gitlab_CI-test--centos6--openjdk13-blue.svg?style=flat

[gitlab-ci-test-centos7-openjdk8]: https://img.shields.io/badge/Gitlab_CI-test--centos7--openjdk8-blue.svg?style=flat
[gitlab-ci-test-centos7-openjdk11]: https://img.shields.io/badge/Gitlab_CI-test--centos7--openjdk11-blue.svg?style=flat
[gitlab-ci-test-centos7-openjdk13]: https://img.shields.io/badge/Gitlab_CI-test--centos7--openjdk13-blue.svg?style=flat

[gitlab-ci-test-ubuntu16.04-openjdk8]: https://img.shields.io/badge/Gitlab_CI-test--ubuntu16.04--openjdk8-blue.svg?style=flat
[gitlab-ci-test-ubuntu16.04-openjdk11]: https://img.shields.io/badge/Gitlab_CI-test--ubuntu16.04--openjdk11-blue.svg?style=flat
[gitlab-ci-test-ubuntu16.04-openjdk13]: https://img.shields.io/badge/Gitlab_CI-test--ubuntu16.04--openjdk13-blue.svg?style=flat

[gitlab-ci-test-ubuntu18.04-openjdk8]: https://img.shields.io/badge/Gitlab_CI-test--ubuntu18.04--openjdk8-blue.svg?style=flat
[gitlab-ci-test-ubuntu18.04-openjdk11]: https://img.shields.io/badge/Gitlab_CI-test--ubuntu18.04--openjdk11-blue.svg?style=flat
[gitlab-ci-test-ubuntu18.04-openjdk13]: https://img.shields.io/badge/Gitlab_CI-test--ubuntu18.04--openjdk13-blue.svg?style=flat

[gitlab-ci-test-debian-stretch-openjdk8]: https://img.shields.io/badge/Gitlab_CI-test--debian--stretch--openjdk8-blue.svg?style=flat
[gitlab-ci-test-debian-stretch-openjdk11]: https://img.shields.io/badge/Gitlab_CI-test--debian--stretch--openjdk11-blue.svg?style=flat
[gitlab-ci-test-debian-stretch-openjdk13]: https://img.shields.io/badge/Gitlab_CI-test--debian--stretch--openjdk13-blue.svg?style=flat

[gitlab-ci-test-debian-buster-openjdk8]: https://img.shields.io/badge/Gitlab_CI-test--debian--buster--openjdk8-blue.svg?style=flat
[gitlab-ci-test-debian-buster-openjdk11]: https://img.shields.io/badge/Gitlab_CI-test--debian--buster--openjdk11-blue.svg?style=flat
[gitlab-ci-test-debian-buster-openjdk13]: https://img.shields.io/badge/Gitlab_CI-test--debian--buster--openjdk13-blue.svg?style=flat

[drone.io-test-arm-openjdk8]: https://img.shields.io/badge/Drone.io-test--arm--openjdk8-blue.svg?style=flat
[drone.io-test-arm-openjdk11]: https://img.shields.io/badge/Drone.io-test--arm--openjdk11-blue.svg?style=flat
[drone.io-test-arm-openjdk13]: https://img.shields.io/badge/Drone.io-test--arm--openjdk13-blue.svg?style=flat

[drone.io-test-arm64-openjdk8]: https://img.shields.io/badge/Drone.io-test--arm64--openjdk8-blue.svg?style=flat
[drone.io-test-arm64-openjdk11]: https://img.shields.io/badge/Drone.io-test--arm64--openjdk11-blue.svg?style=flat
[drone.io-test-arm64-openjdk13]: https://img.shields.io/badge/Drone.io-test--arm64--openjdk13-blue.svg?style=flat

[travis-ci-os-windows]: https://img.shields.io/badge/Travis_CI-OS:_Windows-blue.svg?style=flat
[travis-ci-os-macos]: https://img.shields.io/badge/Travis_CI-OS:_macOS-blue.svg?style=flat

Gradle tasks you should know
----------------------------

`gradle check` runs checkstyle, pmd, junit locally.

`gradle runDockerTests` runs junit tests on all docker containers.

`gradle buildNativeLibs` build the native libs, using our build docker container.

How to make a release
---------------------

```bash
$ ./gradlew clean release
$ git checkout $(git describe --abbrev=0)
$ ./gradlew clean uploadArchives
```

Dont forget to release the artifact from [staging repository](https://oss.sonatype.org/#stagingRepositories)!

License
-------

MIT-License
