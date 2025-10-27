pipeline {
    agent any
    
    triggers {
        // GitHub webhook trigger
        githubPush()
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Docker Info') {
            steps {
                script {
                    // Verify Docker is working
                    sh 'docker --version'
                    sh 'docker info'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    // Build a sample Docker image
                    sh '''
                        echo "FROM alpine:latest" > Dockerfile.temp
                        echo "RUN echo 'Hello from Jenkins Docker!'" >> Dockerfile.temp
                        docker build -f Dockerfile.temp -t jenkins-test-app .
                    '''
                }
            }
        }
        
        stage('Run Docker Container') {
            steps {
                script {
                    // Run the Docker container
                    sh 'docker run --rm jenkins-test-app echo "Container executed successfully!"'
                }
            }
        }
        
        stage('Docker Cleanup') {
            steps {
                script {
                    // Clean up Docker images
                    sh 'docker rmi jenkins-test-app || true'
                    sh 'rm -f Dockerfile.temp'
                }
            }
        }
    }
    
    post {
        always {
            // Clean up Docker system
            sh 'docker system prune -f'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}