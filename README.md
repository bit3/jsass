[![Build Status](https://img.shields.io/travis/bit3/jsass/master.svg?style=flat-square)](https://travis-ci.org/bit3/jsass)
[![Dependency Status](https://www.versioneye.com/user/projects/55171ff6747ccb3c8e000004/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/55171ff6747ccb3c8e000004)
[![Gratipay](https://img.shields.io/gratipay/bit3.svg?style=flat-square)](https://gratipay.com/bit3/)
[![Documentation Status](https://readthedocs.org/projects/jsass/badge/?version=latest)](https://readthedocs.org/projects/jsass/?badge=latest)

Java sass compiler
==================

Feature complete java sass compiler using [libsass][libsass] version 3.2.

The most advantage of jsass is to hide the libsass complexity from the developer and provide a more java like way.

For complete documentation, see [jsass.rtfd.org][jsass-docs].

[libsass]: https://github.com/sass/libsass
[jsass-docs]: http://jsass.rtfd.org/

Compatibility
-------------

| Linux                                 | Windows                     | Mac               |
| ------------------------------------- | --------------------------- | ----------------- |
| ![CentOS 5 (x86_64)][centos5]         | ![Windows 32bit][windows32] | ![OS X 10][osx10] | 
| ![CentOS 6 (x86_64)][centos6]         | ![Windows 64bit][windows64] |                   |
| ![CentOS 7 (x86_64)][centos7]         |                             |                   |
| ![ubuntu 12.04 (x86_64)][ubuntu12.04] |                             |                   |
| ![ubuntu 14.04 (x86_64)][ubuntu14.04] |                             |                   |

A note to Windows and OS X compatibility: jsass may also work on Windows and OS X.
But at the moment there are no automated tests for them. We are working on it.

[centos5]: https://img.shields.io/badge/CentOS-5%20%28x86_64%29-green.svg
[centos6]: https://img.shields.io/badge/CentOS-6%20%28x86_64%29-green.svg
[centos7]: https://img.shields.io/badge/CentOS-7%20%28x86_64%29-green.svg
[ubuntu12.04]: https://img.shields.io/badge/ubuntu-12.04%20%28x86_64%29-green.svg
[ubuntu14.04]: https://img.shields.io/badge/ubuntu-14.04%20%28x86_64%29-green.svg

[windows32]: https://img.shields.io/badge/Windows-32bit-yellow.svg
[windows64]: https://img.shields.io/badge/Windows-64bit-yellow.svg

[osx10]: https://img.shields.io/badge/OS%20X-10-yellow.svg

Gradle tasks you should know
----------------------------

`gradle check` runs checkstyle, pmd, junit locally. Also junit will run on each testing docker container.

`gradle buildNativeLibs` build the native libs, using our build docker container.

`gradle buildDocker` build latest version of all docker containers locally. This should only used for development purpose, the containers are available on Docker Hub!
 
Build scripts
-------------

*Our build scripts are mostly deprecated in favor of the gradle tasks!*

`./bin/make-libjsass-darwin.sh` build the native lib for OS X.
 
`./bin/make-libjsass-linux-x64.sh` build the native lib for Linux, using our build docker container. This is equivalent to `gradle buildDockerBuildLinux64 buildNativeLinux64Libs`!
 
`./bin/make-libjsass-windows-x32.sh` build the native lib for Windows, using our build docker container. This is equivalent to `gradle buildDockerBuildWindows32 buildNativeWindows32Libs`!
 
`./bin/make-libjsass-windows-x64.sh` build the native lib for Windows, using our build docker container. This is equivalent to `gradle buildDockerBuildWindows64 buildNativeWindows64Libs`!
 
License
-------

MIT-License
