FROM centos:5
MAINTAINER "Tristan Lins" <tristan@lins.io>

RUN set -x \
    && yum install -y wget

# Install build tools
RUN set -x \
    && cd /etc/yum.repos.d \
    && wget -nv http://people.centos.org/tru/devtools-1.1/devtools-1.1.repo -O devtools-1.1.repo \
    && yum install -y devtoolset-1.1 devtoolset-1.1-gcc devtoolset-1.1-gcc-c++ glibc glibc-devel \
    && scl enable devtoolset-1.1 bash

# Set PATH, because "scl enable" does not have any effects to "docker build"
ENV PATH $PATH:/opt/centos/devtoolset-1.1/root/usr/bin

# Install cmake 3.7
RUN set -x \
    && DIR=$(mktemp -d) \
    && cd "$DIR" \
    && wget -nv --no-check-certificate http://www.cmake.org/files/v3.7/cmake-3.7.2.tar.gz -O cmake-3.7.2.tar.gz \
    && tar xf cmake-3.7.2.tar.gz \
    && cd cmake-3.7.2 \
    && ./configure \
    && make -j8 \
    && make install \
    && cd / \
    && rm -rf "$DIR"

# Install Java JDK 8
RUN set -x \
    && cd /opt \
    && wget -nv --no-cookies --no-check-certificate \
            --header "Cookie: oraclelicense=accept-securebackup-cookie" \
            "http://download.oracle.com/otn-pub/java/jdk/8u121-b13/e9e7ea248e2c4826b92b3f075a80e441/jdk-8u121-linux-x64.tar.gz" \
            -O jdk-8u121-linux-x64.tar.gz \
    && tar xf jdk-8u121-linux-x64.tar.gz \
    && rm jdk-8u121-linux-x64.tar.gz

# Install git
RUN set -x \
    && cd / \
    && wget -nv https://dl.fedoraproject.org/pub/epel/epel-release-latest-5.noarch.rpm \
    && rpm -i epel-release-latest-5.noarch.rpm \
    && rm epel-release-latest-5.noarch.rpm \
    && yum install -y git

# Install make script
COPY make.sh /

# Run configuration
WORKDIR /jsass
CMD ["/make.sh"]
