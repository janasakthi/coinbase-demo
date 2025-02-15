@echo off
set imageName=your-dockerhub-username/coinbase-demo:latest

echo Building Docker image...
docker build -t %imageName% .

echo Pushing Docker image to Docker Hub...
docker push %imageName%

echo Stopping and removing any existing container...
docker stop coinbase-demo
docker rm coinbase-demo

echo Starting the new container...
docker run -d -p 8080:8080 --name coinbase-demo %imageName%

echo Deployment complete. The application is running at http://localhost:8080
pause

