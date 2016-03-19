#!/bin/bash

docker-machine rm open-tracker
docker-machine create -d parallels --parallels-memory 1024  open-tracker

eval $(docker-machine env open-tracker)

