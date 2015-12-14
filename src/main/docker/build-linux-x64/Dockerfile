FROM centos:5
MAINTAINER "Tristan Lins" <tristan@lins.io>

RUN set -x \
    && yum install -y wget

# Install build tools
RUN set -x \
    && cd /etc/yum.repos.d \
    && wget http://people.centos.org/tru/devtools-1.1/devtools-1.1.repo -O devtools-1.1.repo \
    && yum install -y devtoolset-1.1 devtoolset-1.1-gcc devtoolset-1.1-gcc-c++ glibc glibc-devel \
    && scl enable devtoolset-1.1 bash

# Set PATH, because "scl enable" does not have any effects to "docker build"
ENV PATH $PATH:/opt/centos/devtoolset-1.1/root/usr/bin

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
    && cd / \
    && wget http://packages.sw.be/rpmforge-release/rpmforge-release-0.5.2-2.el5.rf.i386.rpm \
    && rpm --import http://apt.sw.be/RPM-GPG-KEY.dag.txt \
    && rpm -K rpmforge-release-0.5.2-2.el5.rf.i386.rpm \
    && rpm -i rpmforge-release-0.5.2-2.el5.rf.i386.rpm \
    && yum install -y git \
    && rm rpmforge-release-0.5.2-2.el5.rf.i386.rpm

# Install make script
COPY make.sh /

# Run configuration
WORKDIR /jsass
CMD ["/make.sh"]
