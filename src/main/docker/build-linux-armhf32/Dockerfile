FROM ubuntu:16.04
MAINTAINER "Tristan Lins" <tristan@lins.io>

# Install build tools
RUN set -x \
    && apt-get update \
    && apt-get install -y git make cmake gcc-arm-linux-gnueabihf g++-arm-linux-gnueabihf \
    && apt-get clean \
    && rm -r /var/lib/apt/lists/*

# Install Java JDK 8
RUN set -x \
    && apt-get update \
    && apt-get install -y default-jdk openjdk-8-jdk \
    && apt-get clean \
    && rm -r /var/lib/apt/lists/*

# Install make script
COPY make.sh /

# Run configuration
WORKDIR /jsass
CMD ["/make.sh"]
