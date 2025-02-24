pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'your-dockerhub-username/springboot-app:latest'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/your-repo/springboot-app.git'
            }
        }

        stage('Build Jar') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t $DOCKER_IMAGE ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([credentialsId: DOCKER_CREDENTIALS_ID, url: '']) {
                    sh "docker push $DOCKER_IMAGE"
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                sshagent(['ssh-server-credentials']) {
                    sh """
                    ssh user@server-ip <<EOF
                    docker pull $DOCKER_IMAGE
                    docker stop springboot-app || true
                    docker rm springboot-app || true
                    docker run -d -p 8080:8080 --name springboot-app $DOCKER_IMAGE
                    EOF
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful!"
        }
        failure {
            echo "❌ Deployment failed!"
        }
    }
}
