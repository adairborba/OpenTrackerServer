#!/bin/sh
sudo kill -9 `cat caddy.pid`
sudo ./caddy -quiet -pidfile caddy.pid -email drashko@me.com -agree=true -conf=./Caddyfile 
