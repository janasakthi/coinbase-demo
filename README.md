
# Coinbase Demo Application

This is a Spring Boot-based microservice for historical Bitcoin price data. It uses JWT authentication and is containerized with Docker for easy deployment.

---

## Prerequisites

- **Docker** (Installed and running)
- **Git** (For cloning the repository)
- **Docker Hub Account** (For pushing/pulling the image)

---

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/janasakthi/coinbase-demo.git
cd coinbase-demo
```

---

## Docker Setup

### Build and Push the Docker Image

You need to be logged into Docker Hub before pushing the image.

```sh
docker login
```

---

## For Linux & MacOS

### Deploy

1. Make the scripts executable:
    ```sh
    chmod +x deploy.sh shutdown.sh
    ```

2. Run the deployment script:
    ```sh
    ./deploy.sh
    ```

### Shutdown

```sh
./shutdown.sh
```

---

## For Windows

### Using PowerShell

1. Open **PowerShell as Administrator**.

2. Deploy the application:
    ```powershell
    .\deploy.ps1
    ```

3. Shutdown the application:
    ```powershell
    .\shutdown.ps1
    ```

---

### Using Command Prompt (cmd.exe)

1. Open **Command Prompt as Administrator**.

2. Deploy the application:
    ```cmd
    deploy.bat
    ```

3. Shutdown the application:
    ```cmd
    shutdown.bat
    ```

---

## Access the Application

Once deployed, the application will be available at:
```
http://localhost:8080
```

---

## Docker Image Configuration

The Docker image is built using the `Dockerfile` present in the repository. It exposes port `8080`.

Make sure to update the `imageName` in the scripts with your Docker Hub username:

```sh
set imageName=your-dockerhub-username/coinbase-demo:latest
```

---

## Scripts Overview

- **deploy.sh / deploy.ps1 / deploy.bat**: Builds the Docker image, pushes it to Docker Hub, and starts the container.
- **shutdown.sh / shutdown.ps1 / shutdown.bat**: Stops and removes the running container.

---

## Troubleshooting

- Ensure Docker is running before executing any scripts.
- If you face permission issues, try running the shell scripts with `sudo`.
- Make sure you're logged into Docker Hub using `docker login`.

---
