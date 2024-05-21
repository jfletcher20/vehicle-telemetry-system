#!/bin/bash
mvn clean package && mvn cargo:redeploy -P ServerEE-local
