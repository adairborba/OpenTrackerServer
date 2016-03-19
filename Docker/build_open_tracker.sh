#!/bin/sh

#starting open-tracker
docker kill open-tracker
docker rm   open-tracker
docker rmi  open-tracker

docker build -t open-tracker .
docker run -d --name=open-tracker --link mongo:mongo --link mqtt:mqtt --restart=always -p 8080:8080 -p 9999:9999 open-tracker

