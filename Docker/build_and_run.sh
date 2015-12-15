#!/bin/sh

#cleanup docker
docker rm -v $(docker ps -a -q -f status=exited)
docker rmi $(docker images -f "dangling=true" -q)

#starting mosquito server
docker kill mqtt
docker rm   mqtt
docker rmi  toke/mosquitto
docker run -d --name=mqtt --restart=always -tip 1883:1883 -p 9001:9001 toke/mosquitto

#starting mongodb
mkdir -p /opt/data/db
docker kill mongo
docker rm   mongo
docker rmi  mongo
docker run -d --name=mongo --restart=always -v /opt/data/db:/data/db  mongo

#starting open-tracker
chmod +x ./build_open_tracker.sh
./build_open_tracker.sh

docker images
docker ps -a