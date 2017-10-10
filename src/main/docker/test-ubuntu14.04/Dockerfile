FROM ubuntu:14.04
MAINTAINER "Tristan Lins" <tristan@lins.io>

# Install required tools
RUN set -x \
    && apt-get update \
    && apt-get install -y wget unzip \
    && apt-get clean \
    && rm -r /var/lib/apt/lists/*

# Install Java JDK 8
RUN set -x \
    && apt-get update \
    && apt-get install -y software-properties-common \
    && apt-add-repository -y ppa:openjdk-r/ppa \
    && apt-get update \
    && apt-get install -y default-jdk openjdk-8-jdk \
    && apt-get clean \
    && rm -r /var/lib/apt/lists/*

# Install gradle
RUN set -x \
    && cd /opt \
    && wget -nv --no-check-certificate https://services.gradle.org/distributions/gradle-2.14.1-bin.zip \
    && unzip -x gradle-2.14.1-bin.zip \
    && rm gradle-2.14.1-bin.zip

# Setup binaries and java home path
ENV PATH /opt/gradle-2.14.1/bin:$PATH
ENV HOME /tmp
ENV JAVA_HOME /usr/lib/jvm/default-java/jre

# Check java and gradle work properly
RUN set -x \
    && java -version \
    && gradle -version

# Run configuration
WORKDIR "/jsass"
CMD ["/jsass/bin/run-ci-tests.sh"]
