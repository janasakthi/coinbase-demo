# Stop and remove the container
Write-Host "Stopping the container..."
docker stop coinbase-demo
Write-Host "Removing the container..."
docker rm -f coinbase-demo

Write-Host "Container stopped and removed."

