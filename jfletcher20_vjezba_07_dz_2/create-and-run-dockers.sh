#!/bin/bash

docker network create --subnet=20.24.5.0/24 nwtis

docker volume create nwtis_hsql
docker volume create nwtis_h2

sudo cp -R /opt/hsqldb-2.7.2/hsqldb/data/* /var/lib/docker/volumes/nwtis_hsql/_data/

docker build -t nwtis_hsql_i -f Dockerfile.hsql .

docker run -it -d --network=nwtis --ip 20.24.5.3 \
 --name=nwtis_hsql_c --hostname=nwtis_hsql_c \
 --mount source=nwtis_hsql,target=/opt/data \
 nwtis_hsql_i:latest

docker ps
docker stop nwtis_hsql_c

sudo cp -R /opt/database/* /var/lib/docker/volumes/nwtis_h2/_data/

docker build -t nwtis_h2_i -f Dockerfile.h2 .

docker run -it -d --network=nwtis --ip 20.24.5.4 \
-p 9092:9092 \
--name=nwtis_h2_c --hostname=nwtis_h2_c \
--mount source=nwtis_h2,target=/opt/database \
nwtis_h2_i:latest

docker ps
docker stop nwtis_h2_c


docker volume create nwtis_servisi
docker volume create nwtis_app

docker build -t nwtis_servisi_i -f Dockerfile.servisi .
docker build -t nwtis_app_i -f Dockerfile.app .

docker run -it -d --network=nwtis --ip 20.24.5.2 \
 --name=nwtis_app_c --hostname=nwtis_app_c \
 --mount source=nwtis_app,target=/opt/data \
 nwtis_app_i:latest

docker run -it -d --network=nwtis --ip 20.24.5.5 \
 --name=nwtis_servisi_c --hostname=nwtis_servisi_c \
 --mount source=nwtis_servisi,target=/opt/data \
 nwtis_servisi_i:latest

docker ps
docker stop nwtis_app_c
docker stop nwtis_servisi_c
