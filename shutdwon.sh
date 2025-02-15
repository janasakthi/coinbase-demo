#!/bin/bash

# Stop and remove the container
echo "Stopping the container..."
docker stop coinbase-demo || true
echo "Removing the container..."
docker rm -f coinbase-demo || true

echo "Container stopped and removed."

