FROM centos:6
MAINTAINER "Tristan Lins" <tristan@lins.io>

RUN set -x \
    && yum install -y wget

# Install build tools
RUN set -x \
    && cd /etc/yum.repos.d \
    && wget -nv http://people.centos.org/tru/devtools-2/devtools-2.repo -O devtools-2.repo \
    && yum install -y devtoolset-2-binutils devtoolset-2-gcc devtoolset-2-gcc-c++ glibc glibc-devel \
    && scl enable devtoolset-2 bash

# Set PATH, because "scl enable" does not have any effects to "docker build"
ENV PATH $PATH:/opt/rh/devtoolset-2/root/usr/bin

# Install cmake 3.9
RUN set -x \
    && yum install -y tar \
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
    && yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel

# Install git
RUN set -x \
    && cd / \
    && wget -nv https://dl.fedoraproject.org/pub/epel/epel-release-latest-6.noarch.rpm \
    && rpm -i epel-release-latest-6.noarch.rpm \
    && rm epel-release-latest-6.noarch.rpm \
    && yum install -y git

# Install make script
COPY make.sh /

# Run configuration
WORKDIR /jsass
CMD ["/make.sh"]
