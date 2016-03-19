FROM docker.io/java:openjdk-8-jdk

# Define Libsodium version
ENV LIBSODIUM_VERSION 1.0.8

# Define workdir
WORKDIR /root

# Install some tools: gcc build tools, unzip, etc
RUN \
    apt-get update && \
    apt-get -y upgrade && \
    apt-get -y install curl build-essential unzip locate unzip git-core

# Download and install libsodium
# https://download.libsodium.org/doc/

# Download & extract & make libsodium
# Move libsodium build
RUN \
    mkdir -p /tmpbuild/libsodium && \
    cd /tmpbuild/libsodium && \
    curl -L https://download.libsodium.org/libsodium/releases/libsodium-${LIBSODIUM_VERSION}.tar.gz -o libsodium-${LIBSODIUM_VERSION}.tar.gz && \
    tar xfvz libsodium-${LIBSODIUM_VERSION}.tar.gz && \
    cd /tmpbuild/libsodium/libsodium-${LIBSODIUM_VERSION}/ && \
    ./configure && \
    make && make check && \
    make install && \
    mv src/libsodium /usr/local/ && \
    rm -Rf /tmpbuild/

ENV ACTIVATOR_VER 1.3.7

ENV JAVA_OPTS="-Xms512m -Xmx512m -XX:MetaspaceSize=64m"

RUN cd /opt \
    && wget http://downloads.typesafe.com/typesafe-activator/${ACTIVATOR_VER}/typesafe-activator-${ACTIVATOR_VER}.zip \
    && unzip typesafe-activator-${ACTIVATOR_VER}.zip \
    && ln -s /opt/activator-dist-${ACTIVATOR_VER} /opt/activator \
    && rm -f /opt/typesafe-activator-${ACTIVATOR_VER}.zip \
    && ls /opt

RUN cd /opt \
    && git clone https://github.com/dimetron/OpenTrackerServer.git

WORKDIR /opt/OpenTrackerServer

RUN cd /opt/OpenTrackerServer \
    && /opt/activator/activator compile

# Expose ports.
EXPOSE 9999
EXPOSE 8080

# Define an entry point.
ENTRYPOINT ["/opt/activator/activator", "run", "-Dhttp.address=0.0.0.0"]