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
    && wget -nv --no-cookies --no-check-certificate \
            --header "Cookie: oraclelicense=accept-securebackup-cookie" \
            "http://download.oracle.com/otn-pub/java/jdk/8u121-b13/e9e7ea248e2c4826b92b3f075a80e441/jdk-8u121-linux-x64.tar.gz" \
            -O jdk-8u121-linux-x64.tar.gz \
    && tar xf jdk-8u121-linux-x64.tar.gz \
    && rm jdk-8u121-linux-x64.tar.gz

# Install make script
COPY make.sh /

# Run configuration
WORKDIR /jsass
CMD ["/make.sh"]
