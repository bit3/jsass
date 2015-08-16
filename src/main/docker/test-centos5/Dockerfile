FROM centos:5
MAINTAINER "Tristan Lins" <tristan@lins.io>

# Install required tools
RUN set -x \
    && yum install -y wget tar gzip

# Install Java JDK 8
RUN set -x \
    && cd /opt \
    && wget --no-cookies --no-check-certificate \
            --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" \
            "http://download.oracle.com/otn-pub/java/jdk/8u51-b16/jdk-8u51-linux-x64.tar.gz" \
            -O jdk-8u51-linux-x64.tar.gz \
    && tar xf jdk-8u51-linux-x64.tar.gz \
    && rm jdk-8u51-linux-x64.tar.gz

# Install maven 3.3
RUN set -x \
    && cd /opt \
    && wget http://mirror.dkd.de/apache/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz \
    && tar xf apache-maven-3.3.3-bin.tar.gz \
    && rm apache-maven-3.3.3-bin.tar.gz
COPY settings.xml /opt/apache-maven-3.3.3/conf/settings.xml

# Setup binaries and java home path
ENV PATH /opt/apache-maven-3.3.3/bin:/opt/jdk1.8.0_51/bin:$PATH
ENV HOME /tmp
ENV JAVA_HOME /opt/jdk1.8.0_51
ENV M2_HOME /opt/apache-maven-3.3.3
ENV M2 /opt/apache-maven-3.3.3/bin

# Check java and maven work properly
RUN set -x \
    && java -version \
    && mvn -version

# Run configuration
WORKDIR "/jsass"
CMD ["mvn", "test"]
