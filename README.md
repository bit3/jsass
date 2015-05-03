[![Build Status](https://img.shields.io/travis/bit3/jsass/master.svg?style=flat-square)](https://travis-ci.org/bit3/jsass)
[![Dependency Status](https://www.versioneye.com/user/projects/55171ff6747ccb3c8e000004/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/55171ff6747ccb3c8e000004)
[![Gratipay](https://img.shields.io/gratipay/bit3.svg?style=flat-square)](https://gratipay.com/bit3/)
[![Documentation Status](https://readthedocs.org/projects/jsass/badge/?version=latest)](https://readthedocs.org/projects/jsass/?badge=latest)

**EXPERIMENTAL BRANCH**

This is an experimental branch to upgrade to libsass 3.2+, but due too api changes the upgrade is
incomplete and will not work as expected! 

Java sass compiler
==================

Feature complete java sass compiler using [libsass][libsass] version 3.2.

The most advantage of jsass is to hide the libsass complexity from the developer and provide a more java like way.

For complete documentation, see [jsass.rtfd.org](http://jsass.rtfd.org/)

Basic usage example
-------------------

### Compile file

Compiling a file is pretty simple, give an input file and an output file, the rest is just magic.

```java
import CompilationException;
import Compiler;
import Options;

import java.io.File;

public class App {
  public static void main(String[] args) {
    File inputFile = new File("stylesheet.scss");
    File outputFile = new File("stylesheet.css");

    Compiler compiler = new Compiler();
    Options options = new Options();

    try {
      compiler.compileFile(inputFile, outputFile, options);
    } catch (CompilationException e) {
      e.printStackTrace();
    }
  }
}
```

### Compile string

Compiling a string is pretty simple, give an input string, the rest is just magic.
Providing an input file and output file is always a good idea. With this informations libsass can determine the default
include path and calculate relative paths.

```java
import CompilationException;
import Compiler;
import Options;

import java.io.File;

public class App {
  public static void main(String[] args) {
    String input = "body { color: red; }";
    File inputFile = new File("stylesheet.scss");
    File outputFile = new File("stylesheet.css");

    Compiler compiler = new Compiler();
    Options options = new Options();

    try {
      compiler.compileString(input, inputFile, outputFile, options);
    } catch (CompilationException e) {
      e.printStackTrace();
    }
  }
}
```

Rebuild jni adapter
-------------------

```bash
$ mvn com.nativelibs4java:maven-jnaerator-plugin:generate
```

License
-------

MIT-License

Attribution
-----------

This project originally based on [warmuuh/libsass-maven-plugin][warmuuh-libsass-maven-plugin].

[warmuuh-libsass-maven-plugin]: https://github.com/warmuuh/libsass-maven-plugin
[libsass]: https://github.com/sass/libsass
