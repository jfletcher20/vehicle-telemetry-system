#!/bin/bash
cd /home/NWTiS_1/jfletcher20/jfletcher20_vjezba_07_dz_2

mvn clean install

cd *klijenti

mvn cargo:redeploy -P ServerEE-local

cd ../*servisi

java -jar target/jfletcher20_vjezba_07_dz_2_servisi-1.0.0-jar-with-dependencies.jar
