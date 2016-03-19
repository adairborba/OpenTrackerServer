#!/usr/bin/env bash

echo "865733021674619,XXXXXXXXXXX,221015,13144200,50.012703,14.427660,0.00,258.90,342.66,64,19,14.65,1" | nc 10.211.55.7 9999

docker exec -it open-tracker cat /etc/hosts

docker logs open-tracker
docker logs mqtt