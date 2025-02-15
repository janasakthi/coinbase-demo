# Stop and remove the container
Write-Host "Stopping the container..."
docker stop coinbase-demo -ErrorAction SilentlyContinue
Write-Host "Removing the container..."
docker rm coinbase-demo -ErrorAction SilentlyContinue

Write-Host "Container stopped and removed."

