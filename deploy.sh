#!/bin/bash

# Variables
IMAGE_NAME="your-dockerhub-username/coinbase-demo:latest"

# Build the Docker image
echo "Building Docker image..."
docker build -t $IMAGE_NAME .

# Push the Docker image to Docker Hub
echo "Pushing Docker image to Docker Hub..."
docker push $IMAGE_NAME

# Stop and remove any existing container with the same name
echo "Stopping and removing any existing container..."
docker stop coinbase-demo || true
docker rm coinbase-demo || true

# Run the new container
echo "Starting the new container..."
docker run -d -p 8080:8080 --name coinbase-demo $IMAGE_NAME

echo "Deployment complete. The application is running at http://localhost:8080"

