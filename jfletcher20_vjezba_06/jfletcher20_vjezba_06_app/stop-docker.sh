#!/bin/bash

# Check if argument is provided
if [ $# -ne 1 ]; then
    echo "Usage: $0 <image_string>"
    exit 1
fi

# Get the container ID with the specified image string
container_id=$(docker ps -q --filter "ancestor=*:$1")

# Check if container ID is found
if [ -z "$container_id" ]; then
    echo "No container found with image containing '$1'."
    exit 1
fi

# Stop the container
docker stop "$container_id"
