FROM bit3/jsass-build-linux-x64:latest

# Install i686 libraries
RUN set -x \
    && cd / \
    && yum install -y libstdc++.i386 glibc.i686 glibc-devel.i386
