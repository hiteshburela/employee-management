pipeline {

    agent any

    environment {
        MAVEN_HOME = '/opt/maven'
        PATH = "/opt/maven/bin:${env.PATH}"

        APP_NAME = 'employee-management'
        CONTAINER_NAME = 'employee-management'
        DOCKER_IMAGE = 'employee-management:latest'
        DOCKER_NETWORK = 'employee-network'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Maven Build and Test') {
            steps {
                echo 'Running Maven clean, test and package...'

                sh '''
                    echo "Maven version:"
                    /opt/maven/bin/mvn -version

                    echo "Running Maven build..."
                    /opt/maven/bin/mvn clean test package
                '''
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'

                sh '''
                    docker build -t ${DOCKER_IMAGE} .
                '''
            }
        }

        stage('Stop Previous Container') {
            steps {
                echo 'Stopping previous application container if it exists...'

                sh '''
                    if docker ps -q -f name=${CONTAINER_NAME} | grep -q .; then
                        docker stop ${CONTAINER_NAME}
                    fi

                    if docker ps -aq -f name=${CONTAINER_NAME} | grep -q .; then
                        docker rm ${CONTAINER_NAME}
                    fi
                '''
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying new Docker container...'

                sh '''
                    docker run -d \
                      --name ${CONTAINER_NAME} \
                      --network ${DOCKER_NETWORK} \
                      -p 8081:8080 \
                      ${DOCKER_IMAGE}
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                echo 'Waiting for application to start...'

                sh '''
                    sleep 15

                    echo "Running containers:"
                    docker ps

                    echo "Application logs:"
                    docker logs --tail 50 ${CONTAINER_NAME}

                    echo "Testing application..."

                    curl -f http://localhost:8081/api/employees

                    echo "Application verification successful."
                '''
            }
        }
    }

    post {

        success {
            echo 'Pipeline completed successfully. Application deployed successfully.'
        }

        failure {
            echo 'Pipeline failed. Docker deployment was not completed successfully.'
        }

        always {
            echo 'Pipeline execution completed.'
        }
    }
}
