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
    && apt-get update \
    && apt-get install -y software-properties-common \
    && apt-add-repository -y ppa:openjdk-r/ppa \
    && apt-get update \
    && apt-get install -y default-jdk openjdk-8-jdk \
    && apt-get clean \
    && rm -r /var/lib/apt/lists/*

# Install make script
COPY make.sh /

# Run configuration
WORKDIR /jsass
CMD ["/make.sh"]
