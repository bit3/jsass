FROM ubuntu:14.04
MAINTAINER "Tristan Lins" <tristan@lins.io>

# Install required tools
RUN set -x \
    && apt-get install -y wget unzip

# Install Java JDK 8
RUN set -x \
    && cd /opt \
    && wget --no-cookies --no-check-certificate \
            --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" \
            "http://download.oracle.com/otn-pub/java/jdk/8u60-b27/jdk-8u60-linux-x64.tar.gz" \
            -O jdk-8u60-linux-x64.tar.gz \
    && tar xf jdk-8u60-linux-x64.tar.gz \
    && rm jdk-8u60-linux-x64.tar.gz

# Install gradle
RUN set -x \
    && cd /opt \
    && wget --no-check-certificate https://services.gradle.org/distributions/gradle-2.11-bin.zip \
    && unzip -x gradle-2.11-bin.zip \
    && rm gradle-2.11-bin.zip

# Setup binaries and java home path
ENV PATH /opt/gradle-2.11/bin:/opt/jdk1.8.0_60/bin:$PATH
ENV HOME /tmp
ENV JAVA_HOME /opt/jdk1.8.0_60

# Check java and gradle work properly
RUN set -x \
    && java -version \
    && gradle -version

# Run configuration
WORKDIR "/jsass"
CMD ["/jsass/bin/run-ci-tests.sh"]
