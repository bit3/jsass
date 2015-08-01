[![Build Status](https://img.shields.io/travis/bit3/jsass/master.svg?style=flat-square)](https://travis-ci.org/bit3/jsass)
[![Dependency Status](https://www.versioneye.com/user/projects/55171ff6747ccb3c8e000004/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/55171ff6747ccb3c8e000004)
[![Gratipay](https://img.shields.io/gratipay/bit3.svg?style=flat-square)](https://gratipay.com/bit3/)
[![Documentation Status](https://readthedocs.org/projects/jsass/badge/?version=latest)](https://readthedocs.org/projects/jsass/?badge=latest)

Java sass compiler
==================

Feature complete java sass compiler using [libsass][libsass] version 3.2.

The most advantage of jsass is to hide the libsass complexity from the developer and provide a more java like way.

For complete documentation, see [jsass.rtfd.org](http://jsass.rtfd.org/)

Rebuild jni adapter
-------------------

```bash
$ cd src/main/c
$ cmake .
$ make
$ cp libjsass.so ../resources/linux-x86-64/
```

License
-------

MIT-License
