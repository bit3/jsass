FROM andrewd/osxcross
MAINTAINER "Tristan Lins" <tristan@lins.io>

# Install cmake 3.9
RUN set -x \
    && DIR=$(mktemp -d) \
    && cd "$DIR" \
    && wget -nv --no-check-certificate http://www.cmake.org/files/v3.9/cmake-3.9.4.tar.gz -O cmake-3.9.4.tar.gz \
    && tar xf cmake-3.9.4.tar.gz \
    && cd cmake-3.9.4 \
    && ./configure \
    && make -j8 \
    && make install \
    && cd / \
    && rm -rf "$DIR"

# Install Java JDK 8
RUN set -x \
    && echo 'deb http://ftp.debian.org/debian jessie-backports main' >> /etc/apt/sources.list \
    && apt-get update \
    && apt-get install -y openjdk-8-jdk ca-certificates-java=20161107~bpo8+1 \
    && apt-get clean \
    && rm -r /var/lib/apt/lists/*

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
