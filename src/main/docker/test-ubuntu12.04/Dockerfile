FROM ubuntu:12.04
MAINTAINER "Tristan Lins" <tristan@lins.io>

# Install required tools
RUN set -x \
    && apt-get update \
    && apt-get install -y wget unzip \
    && apt-get clean \
    && rm -r /var/lib/apt/lists/*

# Install Java JDK 8
RUN set -x \
    && cd /opt \
    && wget -nv --no-cookies --no-check-certificate \
            --header "Cookie: oraclelicense=accept-securebackup-cookie" \
            "http://download.oracle.com/otn-pub/java/jdk/8u121-b13/e9e7ea248e2c4826b92b3f075a80e441/jdk-8u121-linux-x64.tar.gz" \
            -O jdk-8u121-linux-x64.tar.gz \
    && tar xf jdk-8u121-linux-x64.tar.gz \
    && rm jdk-8u121-linux-x64.tar.gz

# Install gradle
RUN set -x \
    && cd /opt \
    && wget -nv --no-check-certificate https://services.gradle.org/distributions/gradle-2.14.1-bin.zip \
    && unzip -x gradle-2.14.1-bin.zip \
    && rm gradle-2.14.1-bin.zip

# Setup binaries and java home path
ENV PATH /opt/gradle-2.14.1/bin:/opt/jdk1.8.0_121/bin:$PATH
ENV HOME /tmp
ENV JAVA_HOME /opt/jdk1.8.0_121

# Check java and gradle work properly
RUN set -x \
    && java -version \
    && gradle -version

# Run configuration
WORKDIR "/jsass"
CMD ["/jsass/bin/run-ci-tests.sh"]
