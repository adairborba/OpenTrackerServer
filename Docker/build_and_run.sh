#!/bin/sh

#starting mosquito server
sudo docker kill mqtt
sudo docker rm   mqtt
sudo docker rmi  toke/mosquitto
sudo docker run -d --name=mqtt --restart=always -tip 1883:1883 -p 9001:9001 toke/mosquitto

#starting mongodb
sudo docker kill mongo
sudo docker rm   mongo
sudo docker rmi  mongo
sudo docker run -d --name=mongo --restart=always  mongo

#starting open-tracker
chmod +x ./build_open_tracker.sh
./build_open_tracker.sh

sudo docker images
sudo docker ps -a