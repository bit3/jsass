[![pipeline status](https://gitlab.com/jsass/jsass/badges/jsass-6/pipeline.svg)](https://gitlab.com/jsass/jsass/commits/jsass-6)
[![Build Status](https://cloud.drone.io/api/badges/bit3/jsass/status.svg)](https://cloud.drone.io/bit3/jsass)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jsass_jsass&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jsass_jsass)
[![Scrutinizer Code Quality](https://scrutinizer-ci.com/g/bit3/jsass/badges/quality-score.png?b=master)](https://scrutinizer-ci.com/g/bit3/jsass/?branch=master)
[![Code Coverage](https://scrutinizer-ci.com/g/bit3/jsass/badges/coverage.png?b=master)](https://scrutinizer-ci.com/g/bit3/jsass/?branch=master)
[![Known Vulnerabilities](https://snyk.io/test/github/bit3/jsass/badge.svg)](https://snyk.io/test/github/bit3/jsass)

# Java SASS Compiler

The ultimate SASS compiler for Java / the JVM.

## Documentation

- Read the [official documentation](https://jsass.gitlab.io/).
- Check out the [example webapp](./example).
- Inspect the [API documentation](https://javadoc.io/doc/io.bit3/jsass/).

## Developers

### Gradle tasks you should know

`gradle check` runs checkstyle, pmd, junit locally.

`gradle buildNativeLibs` build the native libs, using our build docker container.

### How to make a release

```bash
$ ./gradlew clean release
$ git checkout $(git describe --abbrev=0)
$ ./gradlew clean signMavenPublication publishAllPublicationsToMavenCentralRepository
```

Don't forget to release the artifact from [staging repository](https://oss.sonatype.org/#stagingRepositories)!

## License

[MIT-License](./LICENSE)
