@echo off
echo Stopping the container...
docker stop coinbase-demo

echo Removing the container...
docker rm -f coinbase-demo

echo Container stopped and removed.
pause

