[![pipeline status](https://gitlab.com/jsass/jsass/badges/master/pipeline.svg)](https://gitlab.com/jsass/jsass/commits/master)
[![Build Status](https://img.shields.io/travis/bit3/jsass/master.svg?style=flat&logo=travis)](https://travis-ci.org/bit3/jsass)
[![Documentation Status](https://readthedocs.org/projects/jsass/badge/?version=latest)](http://jsass.readthedocs.io/en/latest/)
[![Javadoc Status](https://javadocio-badges.herokuapp.com/io.bit3/jsass/badge.svg)](http://javadoc.io/doc/io.bit3/jsass/)

Java sass compiler
==================

Feature complete java sass compiler using [libsass][libsass] version 3.5.5.

The most advantage of jsass is to hide the libsass complexity from the developer and provide a more java like way.

For complete documentation, see [jsass.rtfd.org][jsass-docs].

[libsass]: https://github.com/sass/libsass
[jsass-docs]: http://jsass.rtfd.org/

#### Related (third-party) projects

- [Libsass Maven Plugin](https://github.com/warmuuh/libsass-maven-plugin)

Example
-------

There is a [webapp example](example/webapp) containing a servlet sample implementation, to illustrate the way you
may integrate jsass into your webapp.

Changelog
---------

You can find the changelog in our documentation at [jsass.rtfd.org/en/latest/changelog.html][changelog]

[changelog]: http://jsass.readthedocs.org/en/latest/changelog.html

Compatibility
-------------

| Compatibility                         |                                                                     |
| --------------------------------------|---------------------------------------------------------------------|
| ![Java 7][java7]                      | not supported, jsass uses Java 8 features like streams and lambdas! |
| ![Java 8][java8]                      | fully supported                                                     |
| ![Java 11][java11]                    | fully supported and [tested][gitlab-ci] (gitlab ci)                 |
| ![CentOS 6 (x86_64)][centos6]         | fully supported and [tested][gitlab-ci] (gitlab ci)                 |
| ![CentOS 7 (x86_64)][centos7]         | fully supported and [tested][gitlab-ci] (gitlab ci)                 |
| ![ubuntu 12.04 (x86_64)][ubuntu12.04] | fully supported and [tested][gitlab-ci] (gitlab ci)                 |
| ![ubuntu 14.04 (x86_64)][ubuntu14.04] | fully supported and [tested][gitlab-ci] (gitlab ci)                 |
| ![Windows 32bit][windows32]           | not supported                                                       |
| ![Windows 64bit][windows64]           | supported, but not tested                                           |
| ![OS X][osx]                          | fully supported and [tested][travis-ci] (travis ci)                 |
| ![ARM (armhf32)][armhf32]             | experimental, untested                                              |

A note to Windows: jsass may also work on Windows.
But at the moment there are no automated tests and the platform is not well tested!

[java7]: https://img.shields.io/badge/Java-7-red.svg?style=flat
[java8]: https://img.shields.io/badge/Java-8-green.svg?style=flat
[java11]: https://img.shields.io/badge/Java-11-green.svg?style=flat

[centos6]: https://img.shields.io/badge/CentOS-6%20%28x86_64%29-green.svg?style=flat
[centos7]: https://img.shields.io/badge/CentOS-7%20%28x86_64%29-green.svg?style=flat
[ubuntu12.04]: https://img.shields.io/badge/ubuntu-12.04%20%28x86_64%29-green.svg?style=flat
[ubuntu14.04]: https://img.shields.io/badge/ubuntu-14.04%20%28x86_64%29-green.svg?style=flat

[windows32]: https://img.shields.io/badge/Windows-32bit-red.svg?style=flat
[windows64]: https://img.shields.io/badge/Windows-64bit-yellow.svg?style=flat

[osx]: https://img.shields.io/badge/OS%20X-10+-green.svg?style=flat

[armhf32]: https://img.shields.io/badge/ARM_(armhf32)-experimental-yellow.svg?style=flat

[gitlab-ci]: https://gitlab.com/jsass/jsass/pipelines
[travis-ci]: https://travis-ci.org/bit3/jsass

Gradle tasks you should know
----------------------------

`gradle check` runs checkstyle, pmd, junit locally.

`gradle runDockerTests` runs junit tests on all docker containers.

`gradle buildNativeLibs` build the native libs, using our build docker container.

Build scripts
-------------

*Our build scripts are mostly deprecated in favor of the gradle tasks!*

`./bin/make-libjsass-darwin.sh` build the native lib for OS X.
 
`./bin/make-libjsass-linux-x64.sh` build the native lib for Linux, using our build docker container. This is equivalent to `gradle buildNativeLinux64Libs`!
 
`./bin/make-libjsass-windows-x64.sh` build the native lib for Windows, using our build docker container. This is equivalent to `gradle buildNativeWindows64Libs`!
 
How to make a release
---------------------

```bash
$ ./gradlew clean release
$ git checkout $(git describe --abbrev=0)
$ ./gradlew clean uploadArchives
$ git checkout master
$ git merge --no-ff $(git describe --abbrev=0 develop)
$ git push origin master
$ git checkout develop
```

Dont forget to release the artifact from [staging repository](https://oss.sonatype.org/#stagingRepositories)!

License
-------

MIT-License
