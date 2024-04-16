#!/bin/bash

# lists all open ports using java:
#	port 8000 - central server
#	port 801X - radars
lsof -i | grep java
