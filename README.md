[![pipeline status](https://gitlab.com/jsass/jsass/badges/main/pipeline.svg)](https://gitlab.com/jsass/jsass/commits/main)
[![Build Status](https://cloud.drone.io/api/badges/bit3/jsass/status.svg)](https://cloud.drone.io/bit3/jsass)
[![Scrutinizer Code Quality](https://scrutinizer-ci.com/g/bit3/jsass/badges/quality-score.png?b=master)](https://scrutinizer-ci.com/g/bit3/jsass/?branch=master)
[![Code Coverage](https://scrutinizer-ci.com/g/bit3/jsass/badges/coverage.png?b=master)](https://scrutinizer-ci.com/g/bit3/jsass/?branch=master)
[![Known Vulnerabilities](https://snyk.io/test/github/bit3/jsass/badge.svg)](https://snyk.io/test/github/bit3/jsass)

# Java sass compiler

Feature complete java sass compiler.

## Documentation

- Read the [official documentation](https://jsass.gitlab.io/).
- Check out the [example webapp](./example).
- Inspect the [API documentation](https://javadoc.io/doc/io.bit3/jsass/).

## CVE-2022-42889

In October 2022, a critical vulnerability in the commons-text library became known ([CVE-2022-42889](https://securitylab.github.com/advisories/GHSL-2022-018_Apache_Commons_Text/)). jsass uses the commons-text library, but not the affected StringSubstitutor class!
Thus, jsass is not directly affected by this vulnerability.
However, in Jsass version 5.10.5, the commons-text dependency has been updated to version 1.10.0.

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

MIT-License
