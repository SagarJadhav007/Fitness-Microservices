pipeline {
    agent any

    environment {
        AWS_REGION       = "ap-south-1"
        ECR_REGISTRY     = "020641930163.dkr.ecr.ap-south-1.amazonaws.com"
        SERVICE_NAME     = "activityservice"
        IMAGE_NAME       = "${ECR_REGISTRY}/fitx/${SERVICE_NAME}"
        SONAR_PROJECT_KEY = "fitx-activityservice"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                dir('activityservice') {
                    sh './mvnw clean test'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('activityservice') {
                    withSonarQubeEnv('SonarQube') {
                        sh '''
                            ./mvnw sonar:sonar \
                              -Dsonar.projectKey=${SONAR_PROJECT_KEY}
                        '''
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build \
                      -t ${IMAGE_NAME}:${BUILD_NUMBER} \
                      ./activityservice
                '''
            }
        }

        stage('Trivy Scan') {
            steps {
                sh '''
                    trivy image \
                      --severity HIGH,CRITICAL \
                      ${IMAGE_NAME}:${BUILD_NUMBER}
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                sh '''
                    echo "Logging into Amazon ECR..."

                    aws ecr get-login-password \
                      --region ${AWS_REGION} | \
                    docker login \
                      --username AWS \
                      --password-stdin ${ECR_REGISTRY}

                    echo "Pushing image: ${IMAGE_NAME}:${BUILD_NUMBER}"

                    docker push \
                      ${IMAGE_NAME}:${BUILD_NUMBER}

                    echo "Tagging image as latest..."

                    docker tag \
                      ${IMAGE_NAME}:${BUILD_NUMBER} \
                      ${IMAGE_NAME}:latest

                    echo "Pushing latest tag..."

                    docker push \
                      ${IMAGE_NAME}:latest

                    docker logout ${ECR_REGISTRY}
                '''
            }
        }
    }

    post {

        always {
            junit(
                testResults: 'activityservice/target/surefire-reports/*.xml',
                allowEmptyResults: true
            )
        }

        success {
            echo 'FitX Activity Service CI pipeline completed successfully!'
        }

        failure {
            echo 'FitX Activity Service CI pipeline failed.'
        }
    }
}