pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'janasakthi/springboot-app:latest'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/janasakthi/coinbase-demo'
            }
        }

        stage('Build Jar') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    bat "docker build -t $DOCKER_IMAGE ."
                }
            }
        }

       /* stage('Push Docker Image') {
            steps {
				script {
					bat "docker push $DOCKER_IMAGE"
				}
            }
        }*/

		stage('Run Container') {
            steps {
                script {
                    bat """
                    docker stop springboot-app || true
                    docker rm springboot-app || true
                    docker run -d -p 8080:8080 --name springboot-app $DOCKER_IMAGE
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
