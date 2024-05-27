#!/bin/bash
#!/bin/bash

docker network create --subnet=20.24.5.0/24 nwtis


docker rm nwtis_hsql_c
docker image rm nwtis_hsql_i


docker volume create nwtis_hsql

sudo cp -R /opt/hsqldb-2.7.2/hsqldb/data/* /var/lib/docker/volumes/nwtis_hsql/_data/

docker build -t nwtis_hsql_i -f Dockerfile.hsql .

docker run -it -d --network=nwtis --ip 20.24.5.3 \
 --name=nwtis_hsql_c --hostname=nwtis_hsql_c \
 --mount source=nwtis_hsql,target=/opt/data \
 nwtis_hsql_i:latest


docker rm nwtis_h2_c
docker image rm nwtis_h2_i


docker volume create nwtis_h2

sudo cp -R /opt/database/* /var/lib/docker/volumes/nwtis_h2/_data/

docker build -t nwtis_h2_i -f Dockerfile.h2 .

docker run -it -d --network=nwtis --ip 20.24.5.4 \
--name=nwtis_h2_c --hostname=nwtis_h2_c \
--mount source=nwtis_h2,target=/opt/database \
nwtis_h2_i:latest

docker stop nwtis_h2_c




docker rm nwtis_servisi_c
docker image rm nwtis_servisi_i



docker build -t nwtis_servisi_i -f Dockerfile.servisi .

docker run -it -d --network=nwtis --ip 20.24.5.5 \
 -p 9080:8080 \
 --name=nwtis_servisi_c --hostname=nwtis_servisi_c \
 nwtis_servisi_i:latest



docker rm nwtis_app_c
docker image rm nwtis_app_i



docker build -t nwtis_app_i -f Dockerfile.app .

docker run -it -d --network=nwtis --ip 20.24.5.2 \
 --name=nwtis_app_c --hostname=nwtis_app_c \
 nwtis_app_i:latest

docker ps

