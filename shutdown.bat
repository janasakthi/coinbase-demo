@echo off
echo Stopping the container...
docker stop coinbase-demo

echo Removing the container...
docker rm coinbase-demo

echo Container stopped and removed.
pause

