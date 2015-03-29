[![Build Status](https://img.shields.io/travis/bit3/jsass/master.svg?style=flat-square)](https://travis-ci.org/bit3/jsass)
[![Dependency Status](https://www.versioneye.com/user/projects/55171ff6747ccb3c8e000004/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/55171ff6747ccb3c8e000004)

Java sass compiler
==================

Feature complete java sass compiler using [libsass][libsass] version 3.1.

The most advantage of jsass is to hide the libsass complexity from the developer and provide a more java like way.

Basic usage example
-------------------

Compiling a file is pretty simple, give an input file and an output file, the rest is just magic.

### Compile file

```java
import de.bit3.jsass.CompilationException;
import de.bit3.jsass.Compiler;
import de.bit3.jsass.Options;

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
import de.bit3.jsass.CompilationException;
import de.bit3.jsass.Compiler;
import de.bit3.jsass.Options;

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

Options
-------

The options class allow to customize each compilation process. Most options are equals to the well known command line
options from sass compilers.

### Precision

Precision for outputting fractional numbers.

```java
options.setPrecision(6);
```

### Output style

Output style for the generated css code.

```java
options.setOutputStyle(de.bit3.jsass.OutputStyle.NESTED);
```

### Inline source comments

If you want inline source comments.

```java
options.setSourceComments(true);
```

### Embedded source map

Embed sourceMappingUrl as data uri.

```java
options.setSourceMapEmbed(true);
```

### Embed contents in source map

Embed include contents in maps.

```java
options.setSourceMapContents(true);
```

### Omit source map url

Disable sourceMappingUrl in css output

```java
options.setOmitSourceMapUrl(true);
```

### SASS syntax

Treat source_string as sass (as opposed to scss).

```java
options.setIsIndentedSyntaxSrc(true);
```

### Image url

For the image-url Sass function.

```java
options.setImageUrl("img/");
```

### Include paths

```java
options.getIncludePaths().add(new File("bower_components/foundation/scss");
```

### Source map

Path to source map file. Enables the source map generating. Used to create sourceMappingUrl.
     
```java
options.setSourceMapFile(new File("stylesheet.css.map");
```

### Function providers

Register custom function providers.

```java
options.getFunctionProviders().add(new MyFunctions());
```

### Importers

Register custom importers.

```java
options.getImporters().add(new MyImporter());
```

Custom functions
----------------

libsass allow registration of custom functions. These functions are equivalent to `@function` functions in the sass
language. jsass automatically maps methods from any java object to libsass and internally converts java values to
libsass values and vise versa for you.

First you must write an object with public methods.

```java
import de.bit3.jsass.Name;

public class MyFunctions {
    public String hello(@Name("name") String name) {
        return "Hello " + name;
    }
}
```

Then register the object instance to the options.

```java
options.getFunctionProviders().add(new MyFunctions());
```

jsass will map the method `MyFunctions::hello(name)` to libsass as `hello($name)`.

### What methods are registered?

All directly declared public methods are registered as libsass functions.

### Function signature

The function signature is build from the method name and the parameter annotation `@Name`.
If the `@Name` annotation is missing, the name will be `argX`.

### Default values

With the `@Default...Value` annotations, `@DefaultStringValue` for strings for example, you can set a default value.
The default value is passed by libsass to your method. There is no way / need to use method overloading.

### Value types

jsass convert as good as it can the java values to libsass values and vise versa.

#### Java to libsass

| Java type     | Libsass type  | Notes                                     |
| ------------- | ------------- | ----------------------------------------- |
| SassString*   | string        | quoting depend on the SassString settings |
| String        | string        | always quoted with single quotes          |
| Boolean       | boolean       |                                           |
| SassNumber    | double        | unit depend on the SassNumber settings    |
| Number        | double        | without unit                              |
| SassColor     | color         |                                           |
| SassList      | list          | separator depend on the SassList settings |
| Collection    | list          | always with comma separator               |
| Map           | map           |                                           |

#### libsass to Java

| Libsass type  | Parameter type      | resulting Java type | Notes                       |
| ------------- | ------------------- |-------------------- | --------------------------- |
| string        | SassString*         | SassString*         |                             |
| string        | CharSequence        | SassString*         |                             |
| string        | String              | String              | Quotation status get lost   |
| boolean       | Boolean             | Boolean             |                             |
| double        | SassNumber          | SassNumber          |                             |
| double        | Number              | SassNumber          |                             |
| double        | Double              | Double              | unit get lost               |
| double        | Float               | Float               | unit and precision get lost |
| double        | Long                | Long                | unit and fraction get lost  |
| double        | Integer             | Integer             | unit and fraction get lost  |
| double        | Short               | Short               | unit and fraction get lost  |
| double        | Byte                | Byte                | unit and fraction get lost  |
| color         | SassColor           | SassColor           |                             |
| list          | SassList            | SassList            |                             |
| list          | Collection          | SassList            |                             |
| map           | Map<Object, Object> | Map<Object, Object> |                             |

* Note: Remind that `SassString implements CharSequence` which is incompatible with `java.lang.String`.
  If possible it is a good idea to use the `Sass*` type classes, but there is no need.
* Note: Primitive types are also supported. jsass internally only use object types, but thanks to auto-boxing
  primitive type support is also provided.

Custom importers
----------------

TODO

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
