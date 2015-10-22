#!/bin/sh


#starting mongodb
sudo docker kill mongo
sudo docker rm   mongo
sudo docker rmi  mongo
sudo docker run -d --name=mongo--restart=always -h mongodb mongo

#starting open-tracker
sudo docker kill open-tracker
sudo docker rm   open-tracker
sudo docker rmi  open-tracker

sudo docker build  --no-cache -t open-tracker .

sudo docker run -d --name=open-tracker --restart=always --add-host mongo:$MONGOIP -p 8080:8080 -p 9999:9999 open-tracker


sudo docker images