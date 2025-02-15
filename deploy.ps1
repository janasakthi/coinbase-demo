# Variables
$imageName = "your-dockerhub-username/coinbase-demo:latest"

# Build the Docker image
Write-Host "Building Docker image..."
docker build -t $imageName .

# Push the Docker image to Docker Hub
Write-Host "Pushing Docker image to Docker Hub..."
docker push $imageName

# Stop and remove any existing container with the same name
Write-Host "Stopping and removing any existing container..."
docker stop coinbase-demo -ErrorAction SilentlyContinue
docker rm coinbase-demo -ErrorAction SilentlyContinue

# Run the new container
Write-Host "Starting the new container..."
docker run -d -p 8080:8080 --name coinbase-demo $imageName

Write-Host "Deployment complete. The application is running at http://localhost:8080"

