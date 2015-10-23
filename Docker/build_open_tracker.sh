#!/bin/sh

#starting open-tracker
sudo docker kill open-tracker
sudo docker rm   open-tracker
sudo docker rmi  open-tracker

sudo docker build  --no-cache -t open-tracker .
sudo docker run -d --name=open-tracker --restart=always -p 8080:8080 -p 9999:9999 open-tracker
