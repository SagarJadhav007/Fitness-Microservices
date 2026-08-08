pipeline {
    agent any

    environment {
        SERVICE_NAME = "activityservice"
        IMAGE_NAME = "sagarjadhav007/fitx-activityservice"
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
                        sh './mvnw sonar:sonar -Dsonar.projectKey=$SONAR_PROJECT_KEY'
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
                      --exit-code 1 \
                      ${IMAGE_NAME}:${BUILD_NUMBER}
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-fitx',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login \
                          -u "$DOCKER_USERNAME" \
                          --password-stdin

                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}

                        docker tag \
                          ${IMAGE_NAME}:${BUILD_NUMBER} \
                          ${IMAGE_NAME}:latest

                        docker push ${IMAGE_NAME}:latest

                        docker logout
                    '''
                }
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