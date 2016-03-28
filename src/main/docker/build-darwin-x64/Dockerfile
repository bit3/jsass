FROM andrewd/osxcross
MAINTAINER "Tristan Lins" <tristan@lins.io>

# Install cmake 3.2
RUN set -x \
    && DIR=$(mktemp -d) \
    && cd "$DIR" \
    && wget --no-check-certificate http://www.cmake.org/files/v3.2/cmake-3.2.3.tar.gz -O cmake-3.2.3.tar.gz \
    && tar xf cmake-3.2.3.tar.gz \
    && cd cmake-3.2.3 \
    && ./configure \
    && make -j8 \
    && make install \
    && cd / \
    && rm -rf "$DIR"

# Install Java JDK 8
RUN set -x \
    && cd /opt \
    && wget --no-cookies --no-check-certificate \
            --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" \
            "http://download.oracle.com/otn-pub/java/jdk/8u60-b27/jdk-8u60-linux-x64.tar.gz" \
            -O jdk-8u60-linux-x64.tar.gz \
    && tar xf jdk-8u60-linux-x64.tar.gz \
    && rm jdk-8u60-linux-x64.tar.gz

# Install git
RUN set -x \
    && apt-get update \
    && apt-get install -y git \
    && rm -r /var/lib/apt/lists/*

# Install make script
COPY make.sh /

# Run configuration
WORKDIR /jsass
CMD ["/make.sh"]
