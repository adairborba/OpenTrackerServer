FROM docker.io/java:openjdk-8-jdk

ENV ACTIVATOR_VER 1.3.4

RUN apt-get install -y unzip git-core

RUN cd /opt \
 && wget http://downloads.typesafe.com/typesafe-activator/${ACTIVATOR_VER}/typesafe-activator-${ACTIVATOR_VER}.zip \
 && unzip typesafe-activator-${ACTIVATOR_VER}.zip \
 && ln -s /opt/activator-${ACTIVATOR_VER} /opt/activator \
 && rm -f /opt/typesafe-activator-${ACTIVATOR_VER}.zip

RUN cd /opt \
 && git clone https://github.com/dimetron/OpenTrackerServer.git

WORKDIR /opt/OpenTrackerServer

RUN cd /opt/OpenTrackerServer \
 && /opt/activator/activator b

# Expose ports.
EXPOSE 9999

# Define an entry point.
ENTRYPOINT ["/opt/activator/activator", "run", "-Dhttp.address=0.0.0.0"]