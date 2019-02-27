FROM toopher/centos-i386:centos6
MAINTAINER "Tristan Lins" <tristan@lins.io>

# fix container, see comments on https://hub.docker.com/r/toopher/centos-i386/
RUN set -x \
    && sed -i 's/\$arch/i686/g' /etc/yum.repos.d/* \
    && sed -i 's/\$basearch/i386/g' /etc/yum.repos.d/*

# Install required tools
RUN set -x \
    && yum install -y wget unzip git

# Install Java JDK 8
RUN set -x \
    && yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel

# Install gradle
RUN set -x \
    && cd /opt \
    && wget -nv --no-check-certificate https://services.gradle.org/distributions/gradle-2.14.1-bin.zip \
    && unzip -x gradle-2.14.1-bin.zip \
    && rm gradle-2.14.1-bin.zip

# Setup binaries and java home path
ENV PATH /opt/gradle-2.14.1/bin:$PATH
ENV HOME /tmp
ENV JAVA_HOME /usr/lib/jvm/java

# Check java and gradle work properly
RUN set -x \
    && java -version \
    && gradle -version

# Run configuration
WORKDIR "/jsass"
CMD ["/jsass/bin/run-ci-tests.sh"]
