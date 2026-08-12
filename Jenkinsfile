pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Build and Test') {
            steps {
                sh 'mvn clean test package'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t employee-management:latest .'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker rm -f employee-management || true

                    docker run -d \
                      --name employee-management \
                      --network employee-network \
                      -p 8081:8080 \
                      employee-management:latest
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                sh '''
                    sleep 10
                    curl -f http://localhost:8081/api/employees
                '''
            }
        }
    }

    post {
        success {
            echo 'Employee Management deployment successful!'
        }

        failure {
            echo 'Pipeline failed. Docker deployment was not completed successfully.'
        }
    }
}
