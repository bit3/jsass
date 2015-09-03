FROM ubuntu:14.04
MAINTAINER "Tristan Lins" <tristan@lins.io>

# Install required tools
RUN set -x \
    && apt-get update \
    && apt-get install -y software-properties-common \
    && add-apt-repository -y ppa:george-edison55/cmake-3.x \
    && apt-get update \
    && apt-get install -y wget git mingw-w64 make cmake

# Install Java JDK 8
RUN set -x \
    && cd /opt \
    && wget --no-cookies --no-check-certificate \
            --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" \
            "http://download.oracle.com/otn-pub/java/jdk/8u60-b27/jdk-8u60-linux-x64.tar.gz" \
            -O jdk-8u60-linux-x64.tar.gz \
    && tar xf jdk-8u60-linux-x64.tar.gz \
    && rm jdk-8u60-linux-x64.tar.gz

# Install make script
COPY make.sh /

# Run configuration
WORKDIR /jsass
CMD ["/make.sh"]
