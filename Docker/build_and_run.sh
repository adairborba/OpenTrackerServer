#!/bin/sh

sudo docker build  --no-cache open-tracker-server -t open-tracker
sudo docker run -d --name open-tracker --restart=always -p 9999:9999 open-tracker

