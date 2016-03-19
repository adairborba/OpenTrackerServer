#!/bin/bash

docker-machine create -d parallels --parallels-memory 512  open-tracker

eval $(docker-machine env open-tracker)

